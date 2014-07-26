package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.client.builder.message.response.ServerResponse;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.ComponentSpec;
import be.xhibit.teletask.model.spec.Function;

import java.util.List;

public class GetMessage extends GetMessageSupport<ComponentSpec> {
    public GetMessage(ClientConfigSpec clientConfig, Function function, int number) {
        super(function, clientConfig, number);
    }

    @Override
    protected ComponentSpec convertResponse(List<ServerResponse> serverResponses) {
        return this.expectSingleEventMessage(serverResponses);
    }
}
