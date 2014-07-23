package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.model.spec.ClientConfig;
import be.xhibit.teletask.model.spec.Function;

public abstract class FunctionBasedMessageSupport extends MessageSupport {
    private final Function function;

    protected FunctionBasedMessageSupport(ClientConfig clientConfig, Function function) {
        super(clientConfig);
        this.function = function;
    }

    protected Function getFunction() {
        return this.function;
    }
}
