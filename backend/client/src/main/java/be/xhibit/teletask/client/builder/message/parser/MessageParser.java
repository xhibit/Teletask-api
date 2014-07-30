package be.xhibit.teletask.client.builder.message.parser;

import be.xhibit.teletask.client.builder.composer.MessageHandler;
import be.xhibit.teletask.client.builder.message.messages.MessageSupport;
import be.xhibit.teletask.model.spec.ClientConfigSpec;

public interface MessageParser {
    MessageSupport parse(ClientConfigSpec config, MessageHandler messageHandler, byte[] event);
}
