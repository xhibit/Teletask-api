package be.xhibit.teletask.client.message;

import be.xhibit.teletask.model.spec.function.Function;

public class GetMessage extends MessageSupport {
    private final int number;

    public GetMessage(Function function, int number) {
        super(function);
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
