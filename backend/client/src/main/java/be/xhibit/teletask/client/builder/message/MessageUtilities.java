package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.client.builder.ByteUtilities;
import be.xhibit.teletask.client.builder.composer.MessageHandler;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.ComponentSpec;
import com.google.common.primitives.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class MessageUtilities {
    /**
     * Logger responsible for logging and debugging statements.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MessageUtilities.class);
    private static final MessageParser DEFAULT_MESSAGE_PARSER = new DelegatingMessageParser();

    private MessageUtilities() {
    }

    public static List<MessageSupport> receive(Class origin, InputStream inputStream, ClientConfigSpec config, MessageHandler messageHandler, StopCondition stopCondition) throws Exception {
        return receive(origin, inputStream, config, messageHandler, stopCondition, DEFAULT_MESSAGE_PARSER);
    }

    public static List<MessageSupport> receive(Class origin, InputStream inputStream, ClientConfigSpec config, MessageHandler messageHandler, StopCondition stopCondition, MessageParser messageParser) throws Exception {
        List<MessageSupport> responses = new ArrayList<>();

        byte[] overflow = new byte[1];
        long startTime = System.currentTimeMillis();
        while (!stopCondition.isComplete(responses, overflow)) {
            if ((System.currentTimeMillis() - startTime) > 5000) {
                throw new RuntimeException("Did not receive data in a timely fashion. This means either: \n\t- You sent wrong data to the server and hence did not get an acknowledge.\n\t- Or you requested information from the server that was not available to the server");
            }
            int available = inputStream.available();
            if (available > 0) {
                byte[] read = new byte[available];
                inputStream.read(read, 0, available);
                byte[] data = Bytes.concat(overflow, read);
                overflow = extractMessages(origin, config, messageHandler, responses, data, messageParser);
            } else {
                overflow = new byte[0];
            }
            Thread.sleep(10);
        }

        return responses;
    }

    private static byte[] extractMessages(Class origin, ClientConfigSpec config, MessageHandler messageHandler, Collection<MessageSupport> responses, byte[] data, MessageParser messageParser) throws Exception {
        LOG.debug("Receive({}) - Raw bytes: {}", origin.getSimpleName(), ByteUtilities.bytesToHex(data));
        byte[] overflow = new byte[0];
        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            LOG.debug("Receive({}) - Processing byte: {}", origin.getSimpleName(), ByteUtilities.bytesToHex(b));
            if (b == messageHandler.getStxValue()) {
                int eventLengthInclChkSum = data[i + 1] + 1; // +1 for checksum
                byte[] event = new byte[eventLengthInclChkSum];

                if (i + eventLengthInclChkSum > data.length) {
                    overflow = new byte[data.length - i];
                    System.arraycopy(data, i, event, 0, data.length - i);
                    i = data.length - 1;

                    LOG.debug("Receive({}) - Overflowing following byte[]: {}", origin.getSimpleName(), ByteUtilities.bytesToHex(overflow));
                } else {
                    System.arraycopy(data, i, event, 0, eventLengthInclChkSum);

                    i += eventLengthInclChkSum - 1;

                    LOG.debug("Receive({}) - Found message bytes: {}", origin.getSimpleName(), ByteUtilities.bytesToHex(event));
                    try {
                        MessageSupport parse = messageParser.parse(config, messageHandler, event);
                        if (parse != null) {
                            responses.add(parse);
                        }
                    } catch (Exception e) {
                        LOG.error("Exception ({}) caught in readLogResponse: {}", e.getClass().getName(), e.getMessage(), e);
                    }
                }
            } else if (b == messageHandler.getAcknowledgeValue()) {
                responses.add(new AcknowledgeMessage(config));
            } else {
                LOG.warn("Receive({}) - Found byte, but don't know how to handle it: {}", origin.getSimpleName(), ByteUtilities.bytesToHex(b));
            }
        }
        return overflow;
    }

    public interface MessageParser {
        MessageSupport parse(ClientConfigSpec config, MessageHandler messageHandler, byte[] event);
    }

    public static void send(OutputStream outputStream, byte[] message) throws IOException {
        outputStream.write(message);
        outputStream.flush();
    }

    public static ComponentSpec handleEvent(Class origin, ClientConfigSpec config, EventMessage eventMessage) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Event({}): {}", origin.getSimpleName(), eventMessage.getLogInfo(eventMessage.getRawBytes()));
        }
        ComponentSpec component = config.getComponent(eventMessage.getFunction(), eventMessage.getNumber());
        component.setState(eventMessage.getState());
        return component;
    }

    public interface StopCondition {
        boolean isComplete(List<MessageSupport> responses, byte[] overflow);
    }
}
