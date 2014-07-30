package be.xhibit.teletask.client.builder.message.messages.impl;

import be.xhibit.teletask.client.builder.message.messages.MessageSupport;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class AcknowledgeMessage extends MessageSupport {
    /**
     * Logger responsible for logging and debugging statements.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AcknowledgeMessage.class);

    public AcknowledgeMessage(ClientConfigSpec clientConfig) {
        super(clientConfig);
    }

    @Override
    protected byte[] getPayload() {
        return new byte[0];
    }

    @Override
    protected Command getCommand() {
        return Command.ACKNOWLEDGE;
    }

    @Override
    protected String getPayloadLogInfo() {
        return "";
    }

    @Override
    public void execute(OutputStream outputStream) {
        try {
            this.send(outputStream, new byte[] { 10 });
        } catch (IOException e) {
            LOG.error("Exception ({}) caught in execute: {}", e.getClass().getName(), e.getMessage(), e);
        }
    }
}
