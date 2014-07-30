package be.xhibit.teletask.client.builder.composer.config.configurables.command;

import be.xhibit.teletask.client.builder.composer.MessageHandler;
import be.xhibit.teletask.client.builder.composer.config.configurables.CommandConfigurable;
import be.xhibit.teletask.client.builder.message.messages.impl.GetMessage;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;

public class GetCommandConfigurable extends CommandConfigurable<GetMessage> {
    public GetCommandConfigurable(int number, boolean needsCentralUnitParameter, String... paramNames) {
        super(Command.GET, number, needsCentralUnitParameter, paramNames);
    }

    @Override
    public GetMessage parse(ClientConfigSpec config, MessageHandler messageHandler, byte[] rawBytes, byte[] payload) {
        return new GetMessage(config, messageHandler.getFunction(payload[1]), this.getOutputNumber(messageHandler, payload, 2));
    }
}
