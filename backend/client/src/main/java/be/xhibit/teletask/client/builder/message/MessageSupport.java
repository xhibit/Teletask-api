package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.client.builder.ByteUtilities;
import be.xhibit.teletask.client.builder.SendResult;
import be.xhibit.teletask.client.builder.composer.MessageComposer;
import be.xhibit.teletask.client.builder.composer.v2_8.MicrosMessageComposer;
import be.xhibit.teletask.client.builder.composer.v3_1.MicrosPlusMessageComposer;
import be.xhibit.teletask.model.spec.CentralUnitType;
import be.xhibit.teletask.model.spec.ClientConfig;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class MessageSupport {
    /**
     * Logger responsible for logging and debugging statements.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MessageSupport.class);

    private static final Map<CentralUnitType, MessageComposer> COMPOSERS = ImmutableMap.<CentralUnitType, MessageComposer>builder()
            .put(CentralUnitType.MICROS, new MicrosMessageComposer())
            .put(CentralUnitType.MICROS_PLUS, new MicrosPlusMessageComposer())
            .build();

    private static final Pattern REMOVE_NAMES = Pattern.compile("[^\\|]");
    private static final Pattern INSERT_PLACEHOLDERS = Pattern.compile("\\|   ");
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00");

    private final Function function;
    private final ClientConfig clientConfig;

    protected MessageSupport(ClientConfig clientConfig, Function function) {
        this.clientConfig = clientConfig;
        this.function = function;
    }

    public SendResult send(OutputStream outputStream) {
        byte[] message = this.getMessageComposer().compose(this.getCommand(), this.getFunction(), this.getPayload());

        SendResult result;
        try {
            //Send data over socket
            if (Boolean.getBoolean("production")) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Sending: {}", this.format(message));
                }
                outputStream.write(message);
                outputStream.flush();
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Test mode send: {}", this.format(message));
                }
            }

            result = SendResult.SUCCESS;
        } catch (Exception e) {
            result = SendResult.FAILED;
        }

        return result;
    }

    protected MessageComposer getMessageComposer() {
        return COMPOSERS.get(this.getClientConfig().getCentralUnitType());
    }

    protected ClientConfig getClientConfig() {
        return this.clientConfig;
    }

    /**
     * This should return the payload without the function part of the payload.
     *
     * @return The payload after 'function'
     */
    protected abstract byte[] getPayload();

    protected abstract Command getCommand();

    private Function getFunction() {
        return this.function;
    }

    protected String format(byte[] message) {
        List<String> hexParts = this.getHexParts(message);

        StringBuilder table = this.getHeader(hexParts);
        String hexLine = this.getMessageAsTableContent(hexParts, table);
        String seperatorLine = this.getTableSeperatorLine(table.length());
        table.append(System.lineSeparator()).append(seperatorLine);
        table.append(System.lineSeparator()).append(hexLine);
        table.append(System.lineSeparator()).append(seperatorLine);

        return System.lineSeparator() + "Command: " + this.getCommand() + ", Function: " + this.getFunction() + ", Payload: " + this.getPayloadLogInfo() + System.lineSeparator() + seperatorLine + System.lineSeparator() + table.toString();
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
            String paramName = this.getMessageParamName(i);
            if (paramName == null) {
                paramName = "Parameter " + i;
            }
            builder.append(paramName).append(" | ");
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

    private String getMessageParamName(int index) {
        return this.getMessageComposer().getCommandConfig().get(this.getCommand()).getParamNames().get(index);
    }

    protected String formatState(State state) {
        return "State( " + state + " | " + state.getCode() + " | " + ByteUtilities.bytesToHex((byte) state.getCode()) + ")";
    }

    protected String formatOutput(int number) {
        return "Output( " + number + " | " + ByteUtilities.bytesToHex(this.getMessageComposer().composeOutput(number)) + ")";
    }
}
