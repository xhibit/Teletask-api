package be.xhibit.teletask.client.builder.composer.v3_1;

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

import java.nio.ByteBuffer;
import java.util.Map;

public class MicrosPlusMessageHandler extends MessageHandlerSupport {
    /**
     * Logger responsible for logging and debugging statements.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MicrosPlusMessageHandler.class);

    private static final Map<Command, CommandConfig> COMMAND_CONFIG = ImmutableMap.<Command, CommandConfig>builder()
            .put(Command.SET, new CommandConfig(7, "Central Unit", "Fnc", "Outp1", "Outp2", "Sate"))
            .put(Command.GET, new CommandConfig(6, "Central Unit", "Fnc", "Outp1", "Outp2"))
            .put(Command.LOG, new CommandConfig(3, "Central Unit", "Fnc", "Sate"))
            .put(Command.EVENT, new CommandConfig(16, "Central Unit", "Fnc", "Outp1", "Outp2", "Err State", "Sate"))
            .put(Command.KEEP_ALIVE, new CommandConfig(11))
            .build();

    @Override
    public byte[] compose(Command command, byte[] payload) {
        int msgStx = this.getStart();                                       // STX: is this value always fixed 02h?
        int msgLength = 4 + payload.length;                                 // Length: the length of the command without checksum
        int msgCommand = this.getCommandConfig().get(command).getNumber();  // Command Number
        int msgCentralUnit = 1;                                             // Now we only support 1 central unit per

        byte[] messageBytes = Bytes.concat(new byte[]{(byte) msgStx, (byte) msgLength, (byte) msgCommand, (byte) msgCentralUnit}, payload);

        return this.getMessageWithChecksum(messageBytes);
    }

    @Override
    public Map<Command, CommandConfig> getCommandConfig() {
        return COMMAND_CONFIG;
    }

    @Override
    public byte[] composeOutput(int number) {
        return ByteBuffer.allocate(2).putShort((short) number).array();
    }

    @Override
    public void handleEvent(TDSClient client, byte[] eventData) {
        //02 09 10 01 01 00 03 00 00 20
        int counter = 3; //We skip first 4 since they are of no use to us at this time.
        Function function = Function.valueOf(eventData[++counter]);
        int number = ByteBuffer.wrap(new byte[]{eventData[++counter], eventData[++counter]}).getShort();
        ++counter; // This is the ErrorState, not used at this time
        int stateValue = eventData[++counter];
        State state = function.getState(stateValue == -1 ? 255 : stateValue);
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
