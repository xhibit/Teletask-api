package be.xhibit.teletask.client.builder.composer.v3_1;

import be.xhibit.teletask.client.builder.composer.config.ConfigurationSupport;
import be.xhibit.teletask.client.builder.composer.config.configurables.FunctionConfigurable;
import be.xhibit.teletask.client.builder.composer.config.configurables.StateConfigurable;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;
import be.xhibit.teletask.model.spec.StateEnum;
import be.xhibit.teletask.model.spec.StateEnumImpl;
import com.google.common.collect.ImmutableList;

public class MicrosPlusFunctionConfiguration extends ConfigurationSupport<Function, FunctionConfigurable> {
    public MicrosPlusFunctionConfiguration() {
        super(ImmutableList.<FunctionConfigurable>builder()
                .add(new FunctionConfigurable(Function.RELAY, 1))
                .add(new FunctionConfigurable(Function.DIMMER, 2))
                .add(new FunctionConfigurable(Function.MOTOR, 6))
                .add(new FunctionConfigurable(Function.LOCMOOD, 8))
                .add(new FunctionConfigurable(Function.TIMEDMOOD, 9))
                .add(new FunctionConfigurable(Function.GENMOOD, 10))
                .add(new FunctionConfigurable(Function.FLAG, 15))
                .add(new FunctionConfigurable(Function.SENSOR, 20))
                .add(new FunctionConfigurable(Function.COND, 60))
                .build());
    }
}
