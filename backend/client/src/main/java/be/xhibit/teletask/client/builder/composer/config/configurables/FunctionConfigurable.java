package be.xhibit.teletask.client.builder.composer.config.configurables;

import be.xhibit.teletask.client.builder.composer.config.Configurable;
import be.xhibit.teletask.model.spec.Function;

public class FunctionConfigurable extends Configurable<Function> {
    public FunctionConfigurable(Function function, int number) {
        super(number, function);
    }
}
