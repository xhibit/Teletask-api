package be.xhibit.teletask.client.builder.composer.v2_8;

import be.xhibit.teletask.client.TDSClient;
import be.xhibit.teletask.client.builder.CommandConfig;
import be.xhibit.teletask.client.builder.composer.MessageHandlerSupport;
import be.xhibit.teletask.client.builder.message.EventMessage;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MicrosMessageHandler extends MessageHandlerSupport {
    /**
     * Logger responsible for logging and debugging statements.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MicrosMessageHandler.class);

    private static final Map<Command, CommandConfig> COMMAND_CONFIG = ImmutableMap.<Command, CommandConfig>builder()
            .put(Command.SET, new CommandConfig(1, "Fnc", "Outp", "Sate"))
            .put(Command.GET, new CommandConfig(2, "Fnc", "Outp"))
            .put(Command.LOG, new CommandConfig(3, "Fnc", "Sate"))
            .put(Command.EVENT, new CommandConfig(8, "Fnc", "Outp", "Sate"))
            .build();

    @Override
    public byte[] compose(Command command, byte[] payload) {
        int msgStx = this.getStart();                                       // STX: is this value always fixed 02h?
        int msgLength = 3 + payload.length;                                 // Length: the length of the command without checksum
        int msgCommand = this.getCommandConfig().get(command).getNumber();  // Command Number

        byte[] messageBytes = Bytes.concat(new byte[]{(byte) msgStx, (byte) msgLength, (byte) msgCommand}, payload);

        return this.getMessageWithChecksum(messageBytes);
    }

    @Override
    public Map<Command, CommandConfig> getCommandConfig() {
        return COMMAND_CONFIG;
    }

    @Override
    public byte[] composeOutput(int number) {
        return new byte[]{(byte) number};
    }

    @Override
    public void handleEvent(TDSClient client, byte[] eventData) {
        //02 09 10 01 03 00 31
        int counter = 2; //We skip first 3 since they are of no use to us at this time.
        Function function = Function.valueOf(eventData[++counter]);
        int number = eventData[++counter];
        int stateValue = eventData[++counter];
        State state = function.getState(stateValue == -1 ? 255 : stateValue);
//        config.getComponent(Function.valueOf(function), ByteBuffer.wrap(output).getInt());
        EventMessage eventMessage = new EventMessage(client.getConfig(), function, number, state);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Handling event: {}", eventMessage.getLogInfo(eventData));
        }
        client.setState(function, number, state);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }
}
