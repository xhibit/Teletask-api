package be.xhibit.teletask.client.builder.composer.v3_1;

import be.xhibit.teletask.client.builder.composer.config.ConfigurationSupport;
import be.xhibit.teletask.client.builder.composer.config.configurables.CommandConfigurable;
import be.xhibit.teletask.model.spec.Command;
import com.google.common.collect.ImmutableList;

public class MicrosPlusCommandConfiguration extends ConfigurationSupport<Command, CommandConfigurable, Integer> {
    public MicrosPlusCommandConfiguration() {
        super(ImmutableList.<CommandConfigurable>builder()
                .add(new CommandConfigurable(Command.SET, 7, true, "Central Unit", "Fnc", "Output Part 1", "Output Part 2", "State"))
                .add(new CommandConfigurable(Command.GET, 6, true, "Central Unit", "Fnc", "Output Part 1", "Output Part 2"))
                .add(new CommandConfigurable(Command.GROUPGET, 9, true, "Central Unit", "Fnc", "Output Part 1", "Output Part 2"))
                .add(new CommandConfigurable(Command.LOG, 3, false, "Fnc", "State"))
                .add(new CommandConfigurable(Command.EVENT, 16, true, "Central Unit", "Fnc", "Output Part 1", "Output Part 2", "Err State", "State"))
                .add(new CommandConfigurable(Command.KEEP_ALIVE, 11, true))
                .build());
    }

    @Override
    protected Integer getKey(CommandConfigurable configurable) {
        return configurable.getNumber();
    }
}
