package be.xhibit.teletask.client.builder.composer.v2_8;

import be.xhibit.teletask.client.builder.CommandConfig;
import be.xhibit.teletask.client.builder.FunctionConfig;
import be.xhibit.teletask.client.builder.KeepAliveStrategy;
import be.xhibit.teletask.client.builder.StateConfig;
import be.xhibit.teletask.client.builder.composer.MessageHandlerSupport;
import be.xhibit.teletask.client.builder.message.EventMessage;
import be.xhibit.teletask.client.builder.message.GetMessage;
import be.xhibit.teletask.client.builder.message.LogMessage;
import be.xhibit.teletask.client.builder.message.MessageExecutor;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MicrosMessageHandler extends MessageHandlerSupport {
    /**
     * Logger responsible for logging and debugging statements.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MicrosMessageHandler.class);

    public MicrosMessageHandler() {
        super(ImmutableMap.<Command, CommandConfig>builder()
                        .put(Command.SET, new CommandConfig(1, "Fnc", "Output", "State"))
                        .put(Command.GET, new CommandConfig(2, "Fnc", "Output"))
                        .put(Command.LOG, new CommandConfig(3, "Fnc", "Sate"))
                        .put(Command.EVENT, new CommandConfig(8, "Fnc", "Output", "State"))
                        .build(),
                ImmutableMap.<State, StateConfig>builder()
                        .put(State.ON, new StateConfig(255))
                        .put(State.OFF, new StateConfig(0))
                        .put(State.UP, new StateConfig(255))
                        .put(State.DOWN, new StateConfig(0))
                        .build(),
                ImmutableMap.<Function, FunctionConfig>builder()
                        .put(Function.RELAY, new FunctionConfig(1))
                        .put(Function.DIMMER, new FunctionConfig(2))
                        .put(Function.MOTOR, new FunctionConfig(55))
                        .put(Function.LOCMOOD, new FunctionConfig(8))
                        .put(Function.TIMEDMOOD, new FunctionConfig(9))
                        .put(Function.GENMOOD, new FunctionConfig(10))
                        .put(Function.FLAG, new FunctionConfig(15))
                        .put(Function.SENSOR, new FunctionConfig(20))
                        .put(Function.COND, new FunctionConfig(60))
                        .build()
        );
    }

    @Override
    public byte[] compose(Command command, byte[] payload) {
        int msgStx = this.getStxValue();                                       // STX: is this value always fixed 02h?
        int msgLength = 3 + payload.length;                                 // Length: the length of the command without checksum
        int msgCommand = this.getCommandConfig(command).getNumber();        // Command Number

        byte[] messageBytes = Bytes.concat(new byte[]{(byte) msgStx, (byte) msgLength, (byte) msgCommand}, payload);

        return this.getMessageWithChecksum(messageBytes);
    }

    @Override
    public byte[] composeOutput(int... numbers) {
        byte[] outputs = new byte[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            outputs[i] = (byte) numbers[i];

        }
        return outputs;
    }

    @Override
    public EventMessage parseEvent(ClientConfigSpec config, byte[] eventData) {
        //02 09 10 01 03 00 31
        int counter = 2; //We skip first 3 since they are of no use to us at this time.
        Function function = this.getFunction(eventData[++counter]);
        int number = eventData[++counter];
        int stateValue = eventData[++counter];
        State state = this.getState(stateValue == -1 ? 255 : stateValue);
        return new EventMessage(config, eventData, function, number, state);
    }

    @Override
    public List<GetMessage> getGroupGetMessages(ClientConfigSpec config, Function function, int... numbers) {
        List<GetMessage> messages = new ArrayList<>();
        for (int number : numbers) {
            messages.add(new GetMessage(config, function, number));
        }
        return messages;
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    public KeepAliveStrategy getKeepAliveStrategy() {
        return new MicrosKeepAliveStrategy();
    }

    private static class MicrosKeepAliveStrategy implements KeepAliveStrategy {
        @Override
        public int getIntervalMinutes() {
            return 30;
        }

        @Override
        public void execute(ClientConfigSpec config, OutputStream out, InputStream in) throws Exception {
            MessageExecutor.of(new LogMessage(config, Function.MOTOR, State.ON), out, in).call();
        }
    }
}
