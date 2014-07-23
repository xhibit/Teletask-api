package be.xhibit.teletask.client.message;

import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;

import java.util.Map;

public interface MessageComposer {
    byte[] compose(Command command, Function function, byte[] payload);

    Map<Command, CommandConfig> getCommandConfig();

    byte[] composeOutput(int number);
}
