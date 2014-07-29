package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.client.builder.composer.MessageHandler;
import be.xhibit.teletask.model.spec.ClientConfigSpec;

public class DelegatingMessageParser implements MessageUtilities.MessageParser {
    @Override
    public MessageSupport parse(ClientConfigSpec config, MessageHandler messageHandler, byte[] event) {
        return messageHandler.parse(config, event);
    }
}
