package be.xhibit.teletask.client.builder.composer.config.configurables;

import be.xhibit.teletask.client.builder.composer.config.Configurable;
import be.xhibit.teletask.model.spec.State;

public class StateConfigurable extends Configurable<State> {
    public StateConfigurable(State state, int number) {
        super(number, state);
    }
}
