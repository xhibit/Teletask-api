package be.xhibit.teletask.client.builder.message.strategy;

import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.ComponentSpec;
import be.xhibit.teletask.model.spec.Function;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface GroupGetStrategy {
    List<ComponentSpec> execute(ClientConfigSpec config, OutputStream out, InputStream in, Function function, int... numbers) throws Exception;
}
