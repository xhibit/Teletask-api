package be.xhibit.teletask.client.builder.composer.v2_8;

import be.xhibit.teletask.client.builder.composer.config.ConfigurationSupport;
import be.xhibit.teletask.client.builder.composer.config.configurables.StateConfigurable;
import be.xhibit.teletask.client.builder.composer.config.configurables.StateKey;
import be.xhibit.teletask.model.spec.DimmerStateImpl;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;
import be.xhibit.teletask.model.spec.StateEnum;
import be.xhibit.teletask.model.spec.StateEnumImpl;
import com.google.common.collect.ImmutableList;

public class MicrosStateConfiguration extends ConfigurationSupport<State, StateConfigurable, StateKey> {
    public MicrosStateConfiguration() {
        super(createStateList());
    }

    private static Iterable<StateConfigurable> createStateList() {
        ImmutableList.Builder<StateConfigurable> builder = ImmutableList.builder();

        for (Function function : Function.values()) {
            for (State state : function.getStates()) {
                int value = -1;
                if (state instanceof StateEnumImpl) {
                    StateEnum stateEnum = ((StateEnumImpl) state).getState();
                    switch (stateEnum) {
                        case ON:
                        case UP:
                            value = 255;
                            break;
                        case OFF:
                        case DOWN:
                            value = 0;
                            break;
                    }
                } else if (state instanceof DimmerStateImpl) {
                    value = Integer.parseInt(state.getValue());
                }
                if (value > -1) {
                    builder.add(new StateConfigurable(state, value));
                }
            }
        }

        return builder.build();
    }

    @Override
    protected StateKey getKey(StateConfigurable configurable) {
        return new StateKey(configurable.getObject().getFunction(), configurable.getNumber());
    }
}
