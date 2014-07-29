package be.xhibit.teletask.client.builder.composer.config.configurables.command;

import be.xhibit.teletask.client.builder.composer.MessageHandler;
import be.xhibit.teletask.client.builder.composer.config.configurables.CommandConfigurable;
import be.xhibit.teletask.client.builder.message.AcknowledgeMessage;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;

public class AcknowledgeCommandConfigurable extends CommandConfigurable<AcknowledgeMessage> {
    public AcknowledgeCommandConfigurable(int number, boolean needsCentralUnitParameter, String... paramNames) {
        super(Command.ACKNOWLEDGE, number, needsCentralUnitParameter);
    }

    @Override
    public AcknowledgeMessage parse(ClientConfigSpec config, MessageHandler messageHandler, byte[] rawBytes, byte[] payload) {
        return new AcknowledgeMessage(config);
    }
}
