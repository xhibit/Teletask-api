package be.xhibit.teletask.client.message;

import be.xhibit.teletask.model.spec.ClientConfig;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;

public class LogMessage extends MessageSupport {
    private final State state;

    public LogMessage(ClientConfig ClientConfig, Function function, State state) {
        super(ClientConfig, function);
        this.state = state;
    }

    @Override
    protected byte[] getPayload() {
        return new byte[]{(byte) this.state.getCode()};
    }

    @Override
    protected Command getCommand() {
        return Command.SET;
    }

    @Override
    protected String getPayloadLogInfo() {
        return this.formatState(this.state);
    }
}
