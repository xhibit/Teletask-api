package be.xhibit.teletask.client.builder.message.strategy;

import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Function;

import java.io.OutputStream;

public interface GroupGetStrategy {
    void execute(ClientConfigSpec config, OutputStream out, Function function, int... numbers) throws Exception;
}
