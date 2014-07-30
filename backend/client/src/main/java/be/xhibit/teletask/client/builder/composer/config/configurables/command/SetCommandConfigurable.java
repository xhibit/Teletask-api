package be.xhibit.teletask.client.builder.composer.config.configurables.command;

import be.xhibit.teletask.client.builder.composer.MessageHandler;
import be.xhibit.teletask.client.builder.composer.config.configurables.CommandConfigurable;
import be.xhibit.teletask.client.builder.composer.config.configurables.StateKey;
import be.xhibit.teletask.client.builder.message.messages.impl.SetMessage;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;

public class SetCommandConfigurable extends CommandConfigurable<SetMessage> {
    public SetCommandConfigurable(int number, boolean needsCentralUnitParameter, String... paramNames) {
        super(Command.SET, number, needsCentralUnitParameter, paramNames);
    }

    @Override
    public SetMessage parse(ClientConfigSpec config, MessageHandler messageHandler, byte[] rawBytes, byte[] payload) {
        Function function = messageHandler.getFunction(payload[0]);
        return new SetMessage(config, function, this.getOutputNumber(messageHandler, payload, 1), messageHandler.getState(new StateKey(function, payload[messageHandler.getOutputByteSize() + 1])));
    }
}
