package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.client.builder.SendResult;
import be.xhibit.teletask.client.builder.message.response.ServerResponse;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;

import java.util.List;

public class KeepAliveMessage extends MessageSupport<SendResult> {
    public KeepAliveMessage(ClientConfigSpec clientConfig) {
        super(clientConfig);
    }

    @Override
    protected byte[] getPayload() {
        return new byte[0];
    }

    @Override
    public Command getCommand() {
        return Command.KEEP_ALIVE;
    }

    @Override
    protected String getPayloadLogInfo() {
        return "";
    }

    @Override
    protected SendResult convertResponse(List<ServerResponse> serverResponses) {
        return this.expectSingleAcknowledge(serverResponses);
    }
}
