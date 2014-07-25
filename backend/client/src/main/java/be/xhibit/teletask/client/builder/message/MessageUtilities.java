package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.client.builder.ByteUtilities;
import be.xhibit.teletask.client.builder.composer.MessageHandler;
import be.xhibit.teletask.client.builder.message.response.AcknowledgeServerResponse;
import be.xhibit.teletask.client.builder.message.response.EventMessageServerResponse;
import be.xhibit.teletask.client.builder.message.response.ServerResponse;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
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

    private MessageUtilities() {
    }

    public static <T> T receive(InputStream inputStream, ClientConfigSpec config, MessageHandler messageHandler, StopCondition stopCondition, ResponseConverter<T> converter) throws Exception {
        List<ServerResponse> responses = new ArrayList<>();

        byte[] overflow = new byte[0];
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
                overflow = extractResponses(config, messageHandler, responses, data);
            }
            Thread.sleep(10);
        }

        return converter.convert(responses);
    }

    private static byte[] extractResponses(ClientConfigSpec config, MessageHandler messageHandler, Collection<ServerResponse> responses, byte[] data) throws Exception {
        LOG.debug("Raw bytes {}", ByteUtilities.bytesToHex(data));
        byte[] overflow = new byte[0];
        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            LOG.debug("Processing: {}", ByteUtilities.bytesToHex(b));
            if (b == messageHandler.getStxValue()) {
                int eventLengthInclChkSum = data[i + 1] + 1; // +1 for checksum
                byte[] event = new byte[eventLengthInclChkSum];

                if (i + eventLengthInclChkSum > data.length) {
                    overflow = new byte[data.length - i];
                    System.arraycopy(data, i, event, 0, data.length - i);
                    i = data.length - 1;

                    LOG.debug("Overflowing: {}", ByteUtilities.bytesToHex(overflow));
                } else {
                    System.arraycopy(data, i, event, 0, eventLengthInclChkSum);

                    i += eventLengthInclChkSum - 1;

                    LOG.debug("Event bytes part: {}", ByteUtilities.bytesToHex(event));
                    try {
                        responses.add(new EventMessageServerResponse(messageHandler.parseEvent(config, event)));
                    } catch (Exception e) {
                        LOG.error("Exception ({}) caught in readLogResponse: {}", e.getClass().getName(), e.getMessage(), e);
                    }
                }
            } else if (b == messageHandler.getAcknowledgeValue()) {
                responses.add(new AcknowledgeServerResponse());
            }
        }
        return overflow;
    }

    public static void send(OutputStream outputStream, byte[] message) throws IOException {
        outputStream.write(message);
        outputStream.flush();
    }

    public interface StopCondition {
        boolean isComplete(List<ServerResponse> responses, byte[] overflow);
    }

    public interface ResponseConverter<T> {
        T convert(List<ServerResponse> responses);
    }
}
