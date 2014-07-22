package be.xhibit.teletask.client.message;

import be.xhibit.teletask.model.spec.CentralUnitType;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;

public class LogMessage extends MessageSupport {
    private final State state;

    public LogMessage(CentralUnitType centralUnitType, Function function, State state) {
        super(centralUnitType, function);
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
