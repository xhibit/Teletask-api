package be.xhibit.teletask.client.builder.composer.v2_8;

import be.xhibit.teletask.client.builder.composer.config.ConfigurationSupport;
import be.xhibit.teletask.client.builder.composer.config.configurables.CommandConfigurable;
import be.xhibit.teletask.client.builder.composer.config.configurables.FunctionConfigurable;
import be.xhibit.teletask.model.spec.Function;
import com.google.common.collect.ImmutableList;

public class MicrosFunctionConfiguration extends ConfigurationSupport<Function, FunctionConfigurable, Integer> {
    public MicrosFunctionConfiguration() {
        super(ImmutableList.<FunctionConfigurable>builder()
                .add(new FunctionConfigurable(Function.RELAY, 1))
                .add(new FunctionConfigurable(Function.DIMMER, 2))
                .add(new FunctionConfigurable(Function.MOTOR, 55))
                .add(new FunctionConfigurable(Function.LOCMOOD, 8))
                .add(new FunctionConfigurable(Function.TIMEDMOOD, 9))
                .add(new FunctionConfigurable(Function.GENMOOD, 10))
                .add(new FunctionConfigurable(Function.FLAG, 15))
                .add(new FunctionConfigurable(Function.SENSOR, 20))
                .add(new FunctionConfigurable(Function.COND, 60))
                .build());
    }

    @Override
    protected Integer getKey(FunctionConfigurable configurable) {
        return configurable.getNumber();
    }
}
