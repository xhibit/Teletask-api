package be.xhibit.teletask.client.builder.composer;

import be.xhibit.teletask.client.TDSClient;
import be.xhibit.teletask.client.builder.CommandConfig;
import be.xhibit.teletask.client.builder.FunctionConfig;
import be.xhibit.teletask.client.builder.StateConfig;
import be.xhibit.teletask.client.builder.message.GetMessage;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;

import java.util.List;
import java.util.Map;

public interface MessageHandler {
    byte[] compose(Command command, byte[] payload);

    CommandConfig getCommandConfig(Command command);

    StateConfig getStateConfig(State state);

    FunctionConfig getFunctionConfig(Function function);

    byte[] composeOutput(int... number);

    int getStart();

    void handleEvent(TDSClient client, byte[] eventData);

    Function getFunction(int function);

    Command getCommand(int command);

    State getState(int state);

    boolean knowsCommand(Command command);

    String getOutputLogHeaderName(int index);

    List<? extends GetMessage> getGroupGetMessages(ClientConfigSpec config, Function function, int... numbers);
}
