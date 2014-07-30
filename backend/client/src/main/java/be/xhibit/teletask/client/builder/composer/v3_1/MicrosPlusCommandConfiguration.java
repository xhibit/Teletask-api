package be.xhibit.teletask.client.builder.composer.v3_1;

import be.xhibit.teletask.client.builder.composer.MessageHandler;
import be.xhibit.teletask.client.builder.composer.config.ConfigurationSupport;
import be.xhibit.teletask.client.builder.composer.config.configurables.CommandConfigurable;
import be.xhibit.teletask.client.builder.composer.config.configurables.StateKey;
import be.xhibit.teletask.client.builder.composer.config.configurables.command.EventCommandConfigurable;
import be.xhibit.teletask.client.builder.composer.config.configurables.command.GetCommandConfigurable;
import be.xhibit.teletask.client.builder.composer.config.configurables.command.GroupGetCommandConfigurable;
import be.xhibit.teletask.client.builder.composer.config.configurables.command.KeepAliveCommandConfigurable;
import be.xhibit.teletask.client.builder.composer.config.configurables.command.LogCommandConfigurable;
import be.xhibit.teletask.client.builder.composer.config.configurables.command.SetCommandConfigurable;
import be.xhibit.teletask.client.builder.message.messages.impl.EventMessage;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;
import com.google.common.collect.ImmutableList;

public class MicrosPlusCommandConfiguration extends ConfigurationSupport<Command, CommandConfigurable<?>, Integer> {
    public MicrosPlusCommandConfiguration() {
        super(ImmutableList.<CommandConfigurable<?>>builder()
                .add(new SetCommandConfigurable(7, true, "Central Unit", "Fnc", "Output Part 1", "Output Part 2", "State"))
                .add(new GetCommandConfigurable(6, true, "Central Unit", "Fnc", "Output Part 1", "Output Part 2"))
                .add(new GroupGetCommandConfigurable(9, true, "Central Unit", "Fnc", "Output Part 1", "Output Part 2"))
                .add(new LogCommandConfigurable(3, false, "Fnc", "State"))
                .add(new MicrosPlusEventCommandConfigurable())
                .add(new KeepAliveCommandConfigurable(11, true))
                .build());
    }

    @Override
    protected Integer getKey(CommandConfigurable configurable) {
        return configurable.getNumber();
    }

    private static class MicrosPlusEventCommandConfigurable extends EventCommandConfigurable {
        public MicrosPlusEventCommandConfigurable() {
            super(16, true, "Central Unit", "Fnc", "Output Part 1", "Output Part 2", "Err State", "State");
        }

        @Override
        public EventMessage parse(ClientConfigSpec config, MessageHandler messageHandler, byte[] rawBytes, byte[] payload) {
            Function function = messageHandler.getFunction(payload[1]);
            int number = this.getOutputNumber(messageHandler, payload, 2);
            State state = messageHandler.getState(new StateKey(function, payload[5]));
            return new EventMessage(config, rawBytes, function, number, state);
        }
    }
}
