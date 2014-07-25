package be.xhibit.teletask.client.builder.message.strategy;

import be.xhibit.teletask.model.spec.ClientConfigSpec;

import java.io.InputStream;
import java.io.OutputStream;

public interface KeepAliveStrategy {
    int getIntervalMinutes();

    void execute(ClientConfigSpec config, OutputStream out, InputStream in) throws Exception;
}
