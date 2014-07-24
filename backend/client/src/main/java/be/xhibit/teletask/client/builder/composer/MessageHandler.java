package be.xhibit.teletask.client.builder.composer;

import be.xhibit.teletask.client.TDSClient;
import be.xhibit.teletask.client.builder.CommandConfig;
import be.xhibit.teletask.model.spec.ClientConfig;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;

import java.util.Map;

public interface MessageHandler {
    byte[] compose(Command command, byte[] payload);

    Map<Command, CommandConfig> getCommandConfig();

    byte[] composeOutput(int number);

    int getStart();

    void handleEvent(TDSClient client, byte[] eventData);
}
