package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.client.builder.ByteUtilities;
import be.xhibit.teletask.client.builder.SendResult;
import be.xhibit.teletask.client.builder.composer.MessageHandler;
import be.xhibit.teletask.client.builder.composer.MessageHandlerFactory;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public abstract class MessageSupport {
    /**
     * Logger responsible for logging and debugging statements.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MessageSupport.class);

    private static final Pattern REMOVE_NAMES = Pattern.compile("[^\\|]");
    private static final Pattern INSERT_PLACEHOLDERS = Pattern.compile("\\|   ");

    private final ClientConfigSpec clientConfig;

    protected MessageSupport(ClientConfigSpec clientConfig) {
        this.clientConfig = clientConfig;
    }

    public SendResult send(OutputStream outputStream) {
        MessageHandler messageHandler = this.getMessageHandler();
        SendResult result;
        if (this.isValid()) {
            if (messageHandler.knowsCommand(this.getCommand())) {
                byte[] message = messageHandler.compose(this.getCommand(), this.getPayload());

                try {
                    //Send data over socket
                    if (Boolean.getBoolean("production")) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Sending: {}", this.getLogInfo(message));
                        }
                        outputStream.write(message);
                        outputStream.flush();
                    } else {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Test mode send: {}", this.getLogInfo(message));
                        }
                    }

                    result = SendResult.SUCCESS;
                } catch (Exception e) {
                    result = SendResult.FAILED;
                }
            } else {
                result = SendResult.UNKNOW_COMMAND;
            }
        } else {
            result = SendResult.INVALID;
        }

        return result;
    }

    protected boolean isValid() {
        return true;
    }

    protected ClientConfigSpec getClientConfig() {
        return this.clientConfig;
    }

    /**
     * This should return the payload without the function part of the payload.
     *
     * @return The payload after 'function'
     */
    protected abstract byte[] getPayload();

    protected abstract Command getCommand();

    public String getLogInfo(byte[] message) {
        List<String> hexParts = this.getHexParts(message);

        StringBuilder table = this.getHeader(hexParts);
        String hexLine = this.getMessageAsTableContent(hexParts, table);
        String seperatorLine = this.getTableSeperatorLine(table.length());
        table.append(System.lineSeparator()).append(seperatorLine);
        table.append(System.lineSeparator()).append(hexLine);
        table.append(System.lineSeparator()).append(seperatorLine);

        return System.lineSeparator() + "Command: " + this.getCommand() + ", Payload: " + this.getPayloadLogInfo() + System.lineSeparator() + "Raw Bytes: " + ByteUtilities.bytesToHex(message) + System.lineSeparator() + seperatorLine + System.lineSeparator() + table.toString();
    }

    protected abstract String getPayloadLogInfo();

    private String getTableSeperatorLine(int size) {
        return Strings.repeat("-", size);
    }

    private String getMessageAsTableContent(List<String> parts, StringBuilder builder) {
        String content = builder.toString();
        content = REMOVE_NAMES.matcher(content).replaceAll(" ");
        content = INSERT_PLACEHOLDERS.matcher(content).replaceAll("| %s");
        content = String.format(content, parts.toArray());
        return content;
    }

    private StringBuilder getHeader(Collection<String> parts) {
        StringBuilder builder = new StringBuilder(500);
        builder.append("| STX | Length | Command | ");
        for (int i = 1; i <= parts.size() - 4; i++) {
            String logHeaderName = this.getLogHeaderName(i);
            if (logHeaderName == null) {
                logHeaderName = "Parameter " + i;
            }
            builder.append(logHeaderName).append(" | ");
        }
        builder.append("ChkSm |");
        return builder;
    }

    private List<String> getHexParts(byte[] message) {
        return Splitter.on(' ')
                .trimResults()
                .omitEmptyStrings()
                .splitToList(ByteUtilities.bytesToHex(message));
    }

    protected String getLogHeaderName(int index) {
        return this.getMessageHandler().getCommandConfig(this.getCommand()).getParamNames().get(index);
    }

    protected String formatState(State... states) {
        Collection<String> log = new ArrayList<>();
        for (State state : states) {
            log.add("State( " + state + " | " + (state == null ? null : this.getMessageHandler().getStateConfig(state).getNumber()) + " | " + (state == null ? null : ByteUtilities.bytesToHex((byte) this.getMessageHandler().getStateConfig(state).getNumber())) + ")");
        }
        return Joiner.on(", ").join(log);
    }

    protected MessageHandler getMessageHandler() {
        return MessageHandlerFactory.getMessageHandler(this.getClientConfig().getCentralUnitType());
    }

    protected String formatFunction(Function function) {
        return "Function( " + function + " | " + this.getMessageHandler().getFunctionConfig(function).getNumber() + " | " + ByteUtilities.bytesToHex((byte) this.getMessageHandler().getFunctionConfig(function).getNumber()) + ")";
    }

    protected String formatOutput(int... numbers) {
        Collection<String> outputs = new ArrayList<>();
        for (int number : numbers) {
            outputs.add("Output( " + number + " | " + ByteUtilities.bytesToHex(this.getMessageHandler().composeOutput(number)) + ")");
        }
        return Joiner.on(", ").join(outputs);
    }
}
