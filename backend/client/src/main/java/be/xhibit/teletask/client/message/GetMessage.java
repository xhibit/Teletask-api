package be.xhibit.teletask.client.message;

import be.xhibit.teletask.model.spec.CentralUnitType;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;

public class GetMessage extends MessageSupport {
    private final int number;

    public GetMessage(CentralUnitType centralUnitType, Function function, int number) {
        super(centralUnitType, function);
        this.number = number;
    }

    @Override
    protected byte[] getPayload() {
        return new byte[]{(byte) this.number};
    }

    @Override
    protected Command getCommand() {
        return Command.GET;
    }
}
