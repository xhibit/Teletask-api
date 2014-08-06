package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.client.builder.ByteUtilities;
import be.xhibit.teletask.client.builder.composer.MessageHandler;
import be.xhibit.teletask.client.builder.message.messages.MessageSupport;
import be.xhibit.teletask.client.builder.message.parser.DelegatingMessageParser;
import be.xhibit.teletask.client.builder.message.parser.MessageParser;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import com.google.common.primitives.Bytes;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class MessageUtilities {
    private static final MessageParser DEFAULT_MESSAGE_PARSER = new DelegatingMessageParser();

    private MessageUtilities() {
    }

    public static List<MessageSupport> receive(Logger logger, InputStream inputStream, ClientConfigSpec config, MessageHandler messageHandler) throws Exception {
        return receive(logger, inputStream, config, messageHandler, DEFAULT_MESSAGE_PARSER);
    }

    public static List<MessageSupport> receive(Logger logger, InputStream inputStream, ClientConfigSpec config, MessageHandler messageHandler, MessageParser messageParser) throws Exception {
        List<MessageSupport> responses = new ArrayList<>();

        byte[] overflow = null;
        long startTime = System.currentTimeMillis();
        while (overflow == null || overflow.length > 0) {
            if ((System.currentTimeMillis() - startTime) > 5000) {
                throw new RuntimeException("Did not receive data in a timely fashion. This means either: \n\t- You sent wrong data to the server and hence did not get an acknowledge.\n\t- Or you requested information from the server that was not available to the server");
            }
            int available = inputStream.available();
            if (available > 0) {
                byte[] read = new byte[available];
                inputStream.read(read, 0, available);
                byte[] data = overflow == null ? read : Bytes.concat(overflow, read);
                overflow = extractMessages(logger, config, messageHandler, responses, data, messageParser);
            } else {
                overflow = new byte[0];
            }
            Thread.sleep(10);
        }

        return responses;
    }

    private static byte[] extractMessages(Logger logger, ClientConfigSpec config, MessageHandler messageHandler, Collection<MessageSupport> responses, byte[] data, MessageParser messageParser) throws Exception {
        logger.debug("Receive - Raw bytes: {}", ByteUtilities.bytesToHex(data));
        byte[] overflow = new byte[0];
        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            logger.debug("Receive - Processing byte: {}", ByteUtilities.bytesToHex(b));
            if (b == messageHandler.getStxValue()) {
                int eventLengthInclChkSum = data[i + 1] + 1; // +1 for checksum
                byte[] event = new byte[eventLengthInclChkSum];

                if (i + eventLengthInclChkSum > data.length) {
                    overflow = new byte[data.length - i];
                    System.arraycopy(data, i, event, 0, data.length - i);
                    i = data.length - 1;

                    logger.debug("Receive - Overflowing following byte[]: {}", ByteUtilities.bytesToHex(overflow));
                } else {
                    System.arraycopy(data, i, event, 0, eventLengthInclChkSum);

                    i += eventLengthInclChkSum - 1;

                    logger.debug("Receive - Found message bytes: {}", ByteUtilities.bytesToHex(event));
                    try {
                        MessageSupport parse = messageParser.parse(config, messageHandler, event);
                        if (parse != null) {
                            responses.add(parse);
                        }
                    } catch (Exception e) {
                        logger.error("Exception ({}) caught in readLogResponse: {}", e.getClass().getName(), e.getMessage(), e);
                    }
                }
            } else if (b == messageHandler.getAcknowledgeValue()) {
                logger.debug("Received acknowledge");
            } else {
                logger.warn("Receive - Found byte, but don't know how to handle it: {}", ByteUtilities.bytesToHex(b));
            }
        }
        return overflow;
    }

    public static void send(OutputStream outputStream, byte[] message) throws IOException {
        outputStream.write(message);
        outputStream.flush();
    }
}
