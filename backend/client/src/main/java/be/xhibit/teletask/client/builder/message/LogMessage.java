package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.model.spec.ClientConfig;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;
import com.google.common.base.Joiner;

public class LogMessage extends FunctionBasedMessageSupport {
    private final State state;

    public LogMessage(ClientConfig ClientConfig, Function function, State state) {
        super(ClientConfig, function);
        this.state = state;
    }

    @Override
    protected byte[] getPayload() {
        return new byte[]{this.getFunction().getCode(), (byte) this.state.getCode()};
    }

    @Override
    protected Command getCommand() {
        return Command.LOG;
    }

    @Override
    protected String getPayloadLogInfo() {
        return Joiner.on(", ").join(this.formatFunction(this.getFunction()), this.formatState(this.state));
    }
}
