package be.xhibit.teletask.model.spec;

public interface MessageComposer {
    byte[] compose(Command command, Function function, byte[] payload);
}
