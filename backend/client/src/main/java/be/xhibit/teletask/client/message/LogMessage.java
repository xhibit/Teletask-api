package be.xhibit.teletask.client.message;

import be.xhibit.teletask.model.spec.State;
import be.xhibit.teletask.model.spec.Function;

public class LogMessage extends MessageSupport {
    private final State state;

    public LogMessage(Function function, State state) {
        super(function);
        this.state = state;
    }

    @Override
    protected byte[] getPayload() {
        return new byte[]{this.state.getCode()};
    }

    @Override
    protected Command getCommand() {
        return Command.SET;
    }
}
