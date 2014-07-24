package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;

public abstract class FunctionStateBasedMessageSupport extends FunctionBasedMessageSupport {
    private final State state;

    protected FunctionStateBasedMessageSupport(ClientConfigSpec clientConfig, Function function, State state) {
        super(clientConfig, function);
        this.state = state;
    }

    public State getState() {
        return this.state;
    }

    @Override
    protected boolean isValid() {
        return this.getFunction().getStates().contains(this.getState());
    }
}
