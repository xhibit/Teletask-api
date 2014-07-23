package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.model.spec.ClientConfig;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;

public class GetMessage extends MessageSupport {
    private final int number;

    public GetMessage(ClientConfig clientConfig, Function function, int number) {
        super(clientConfig, function);
        this.number = number;
    }

    @Override
    protected byte[] getPayload() {
        return this.getMessageComposer().composeOutput(this.number);
    }

    @Override
    protected Command getCommand() {
        return Command.GET;
    }

    @Override
    protected String getPayloadLogInfo() {
        return this.formatOutput(this.number);
    }
}
