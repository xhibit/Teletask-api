package be.xhibit.teletask.client.builder.composer.config;

import be.xhibit.teletask.client.builder.composer.config.configurables.CommandConfigurable;
import be.xhibit.teletask.model.spec.Command;

public abstract class CommandConfiguration extends ConfigurationSupport<Command, CommandConfigurable> {
    public CommandConfiguration(Iterable<CommandConfigurable> config) {
        super(config);
    }
}