package be.xhibit.teletask.client.builder.composer.v2_8;

import be.xhibit.teletask.client.builder.composer.config.ConfigurationSupport;
import be.xhibit.teletask.client.builder.composer.config.configurables.StateConfigurable;
import be.xhibit.teletask.model.spec.State;
import be.xhibit.teletask.model.spec.StateEnum;
import be.xhibit.teletask.model.spec.StateEnumImpl;
import com.google.common.collect.ImmutableList;

public class MicrosStateConfiguration extends ConfigurationSupport<State, StateConfigurable> {
    public MicrosStateConfiguration() {
        super(ImmutableList.<StateConfigurable>builder()
                .add(new StateConfigurable(new StateEnumImpl(StateEnum.ON), 255))
                .add(new StateConfigurable(new StateEnumImpl(StateEnum.OFF), 0))
                .add(new StateConfigurable(new StateEnumImpl(StateEnum.UP), 255))
                .add(new StateConfigurable(new StateEnumImpl(StateEnum.DOWN), 0))
                .build());
    }
}
