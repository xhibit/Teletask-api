package be.xhibit.teletask.client.builder.composer.v3_1;

import be.xhibit.teletask.client.builder.composer.config.ConfigurationSupport;
import be.xhibit.teletask.client.builder.composer.config.configurables.StateConfigurable;
import be.xhibit.teletask.model.spec.DimmerStateImpl;
import be.xhibit.teletask.model.spec.State;
import be.xhibit.teletask.model.spec.StateEnum;
import be.xhibit.teletask.model.spec.StateEnumImpl;
import com.google.common.collect.ImmutableList;

public class MicrosPlusStateConfiguration extends ConfigurationSupport<State, StateConfigurable> {
    public MicrosPlusStateConfiguration() {
        super(createStateList());
    }

    private static Iterable<StateConfigurable> createStateList() {
        ImmutableList.Builder<StateConfigurable> builder = ImmutableList.<StateConfigurable>builder();
        for (int i = 0; i <= 100; i++) {
            builder.add(new StateConfigurable(new DimmerStateImpl(i), i));
        }
        return builder
                .add(new StateConfigurable(new StateEnumImpl(StateEnum.ON), 255))
                .add(new StateConfigurable(new StateEnumImpl(StateEnum.OFF), 0))
                .add(new StateConfigurable(new StateEnumImpl(StateEnum.UP), 1))
                .add(new StateConfigurable(new StateEnumImpl(StateEnum.DOWN), 2))
                .add(new StateConfigurable(new StateEnumImpl(StateEnum.STOP), 3))
                .add(new StateConfigurable(new StateEnumImpl(StateEnum.TOGGLE), 103))
                .build();
    }
}
