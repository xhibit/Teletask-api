package be.xhibit.teletask.client.message;

import be.xhibit.teletask.model.spec.CentralUnitType;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;

public class SetMessage extends MessageSupport {
    private final int number;
    private final State state;

    public SetMessage(CentralUnitType centralUnitType, Function function, int number, State state) {
        super(centralUnitType, function);
        this.number = number;
        this.state = state;
    }

    @Override
    protected byte[] getPayload() {
        return new byte[]{(byte) this.number, this.state.getCode()};
    }

    @Override
    protected Command getCommand() {
        return Command.SET;
    }
}
