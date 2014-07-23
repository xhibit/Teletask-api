package be.xhibit.teletask.client.message;

import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;

public interface MessageComposer {
    byte[] compose(Command command, Function function, byte[] payload);
}
