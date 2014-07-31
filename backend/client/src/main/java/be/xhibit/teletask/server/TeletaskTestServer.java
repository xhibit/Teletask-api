package be.xhibit.teletask.server;

import be.xhibit.teletask.client.builder.ByteUtilities;
import be.xhibit.teletask.client.builder.composer.MessageHandler;
import be.xhibit.teletask.client.builder.message.MessageUtilities;
import be.xhibit.teletask.client.builder.message.messages.MessageSupport;
import be.xhibit.teletask.client.builder.message.messages.impl.EventMessage;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    private ServerSocket server;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private final Timer timer = new Timer();

    public TeletaskTestServer(int port, ClientConfigSpec config, MessageHandler messageHandler) {
        this.port = port;
        this.config = config;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {
            this.server = new ServerSocket(this.getPort());
            this.socket = this.server.accept();
            this.inputStream = this.socket.getInputStream();
            this.outputStream = this.socket.getOutputStream();
            this.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        List<MessageSupport> messages = MessageUtilities.receive(LOG, TeletaskTestServer.this.inputStream, TeletaskTestServer.this.getConfig(), TeletaskTestServer.this.getMessageHandler());
                        for (MessageSupport message : messages) {
                            LOG.debug("Processing message: {}", message.toString());
                            TeletaskTestServer.this.outputStream.write(new byte[]{10});
                            List<EventMessage> eventMessages = message.respond(TeletaskTestServer.this.getConfig(), TeletaskTestServer.this.getMessageHandler());
                            if (eventMessages != null) {
                                for (EventMessage eventMessage : eventMessages) {
                                    LOG.debug("Sending bytes to client: {}", ByteUtilities.bytesToHex(eventMessage.getRawBytes()));
                                    TeletaskTestServer.this.outputStream.write(eventMessage.getRawBytes());
                                    TeletaskTestServer.this.outputStream.flush();
                                }
                            }
                            TeletaskTestServer.this.outputStream.flush();
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

    public void stop() {
        LOG.debug("Stopping test server...");
        try {
            this.timer.purge();
            this.timer.cancel();
            this.inputStream.close();
            this.outputStream.close();
            this.socket.close();
            this.server.close();
        } catch (IOException e) {
            LOG.error("Exception ({}) caught in stop: {}", e.getClass().getName(), e.getMessage(), e);
        }
        LOG.debug("Stopped test server.");
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