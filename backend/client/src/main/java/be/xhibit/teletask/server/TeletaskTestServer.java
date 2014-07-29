package be.xhibit.teletask.server;

import be.xhibit.teletask.client.builder.composer.MessageHandler;
import be.xhibit.teletask.client.builder.message.MessageSupport;
import be.xhibit.teletask.client.builder.message.MessageUtilities;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TeletaskTestServer implements Runnable {
    /**
     * Logger responsible for logging and debugging statements.
     */
    private static final Logger LOG = LoggerFactory.getLogger(TeletaskTestServer.class);

    private final int port;
    private final ClientConfigSpec config;
    private final MessageHandler messageHandler;

    public TeletaskTestServer(int port, ClientConfigSpec config, MessageHandler messageHandler) {
        this.port = port;
        this.config = config;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try (
                ServerSocket server = new ServerSocket(this.getPort());
        ) {
            Socket socket = server.accept();
            final InputStream inputStream = socket.getInputStream();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        List<MessageSupport> messages = MessageUtilities.receive(TeletaskTestServer.class, inputStream, TeletaskTestServer.this.getConfig(), TeletaskTestServer.this.getMessageHandler(), new MessageUtilities.StopCondition() {
                            @Override
                            public boolean isComplete(List<MessageSupport> responses, byte[] overflow) {
                                return overflow.length == 0;
                            }
                        });
                        for (MessageSupport message: messages) {
                            //TODO: respond with test response!
                            LOG.debug(message.toString());
                        }
                    } catch (Exception e) {
                        LOG.error("Exception ({}) caught in run: {}", e.getClass().getName(), e.getMessage(), e);
                    }
                }
            }, 100, 10);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPort() {
        return this.port;
    }

    public ClientConfigSpec getConfig() {
        return this.config;
    }

    public MessageHandler getMessageHandler() {
        return this.messageHandler;
    }
}