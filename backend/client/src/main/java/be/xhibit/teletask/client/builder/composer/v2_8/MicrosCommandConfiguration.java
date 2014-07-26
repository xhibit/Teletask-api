package be.xhibit.teletask.client.builder.composer.v2_8;

import be.xhibit.teletask.client.builder.composer.config.ConfigurationSupport;
import be.xhibit.teletask.client.builder.composer.config.configurables.CommandConfigurable;
import be.xhibit.teletask.model.spec.Command;
import com.google.common.collect.ImmutableList;

public class MicrosCommandConfiguration extends ConfigurationSupport<Command, CommandConfigurable> {
    public MicrosCommandConfiguration() {
        super(ImmutableList.<CommandConfigurable>builder()
                .add(new CommandConfigurable(Command.SET, 1, false, "Fnc", "Output", "State"))
                .add(new CommandConfigurable(Command.GET, 2, false, "Fnc", "Output"))
                .add(new CommandConfigurable(Command.LOG, 3, false, "Fnc", "Sate"))
                .add(new CommandConfigurable(Command.EVENT, 8, false, "Fnc", "Output", "State"))
                .build());
    }
}
