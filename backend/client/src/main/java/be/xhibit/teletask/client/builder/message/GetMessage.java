package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.ComponentSpec;
import be.xhibit.teletask.model.spec.Function;

public class GetMessage extends GetMessageSupport<ComponentSpec> {
    public GetMessage(ClientConfigSpec clientConfig, Function function, int number) {
        super(function, clientConfig, number);
    }
}
