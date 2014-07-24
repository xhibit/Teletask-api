package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.client.builder.composer.MessageHandlerFactory;
import be.xhibit.teletask.model.spec.ClientConfig;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;
import com.google.common.base.Joiner;
import com.google.common.primitives.Bytes;

public class EventMessage extends FunctionBasedMessageSupport {
    private final int number;
    private final State state;

    public EventMessage(ClientConfig clientConfig, Function function, int number, State state) {
        super(clientConfig, function);
        this.number = number;
        this.state = state;
    }

    @Override
    protected byte[] getPayload() {
        return Bytes.concat(new byte[]{this.getFunction().getCode()}, MessageHandlerFactory.getMessageHandler(this.getClientConfig().getCentralUnitType()).composeOutput(this.number), new byte[]{(byte) this.state.getCode()});
    }

    @Override
    protected Command getCommand() {
        return Command.EVENT;
    }

    @Override
    protected String getPayloadLogInfo() {
        return Joiner.on(", ").join(this.formatFunction(this.getFunction()), this.formatOutput(this.number), this.formatState(this.state));
    }
}
