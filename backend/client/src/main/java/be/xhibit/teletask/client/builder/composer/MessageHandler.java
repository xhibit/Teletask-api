package be.xhibit.teletask.client.builder.composer;

import be.xhibit.teletask.client.builder.CommandConfig;
import be.xhibit.teletask.client.builder.FunctionConfig;
import be.xhibit.teletask.client.builder.KeepAliveStrategy;
import be.xhibit.teletask.client.builder.StateConfig;
import be.xhibit.teletask.client.builder.message.EventMessage;
import be.xhibit.teletask.client.builder.message.GetMessageSupport;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;

import java.util.List;

public interface MessageHandler {
    byte[] compose(Command command, byte[] payload);

    CommandConfig getCommandConfig(Command command);

    StateConfig getStateConfig(State state);

    FunctionConfig getFunctionConfig(Function function);

    byte[] composeOutput(int... number);

    int getStxValue();

    EventMessage parseEvent(ClientConfigSpec config, byte[] eventData);

    Function getFunction(int function);

    Command getCommand(int command);

    State getState(int state);

    boolean knowsCommand(Command command);

    String getOutputLogHeaderName(int index);

    List<? extends GetMessageSupport> getGroupGetMessages(ClientConfigSpec config, Function function, int... numbers);

    KeepAliveStrategy getKeepAliveStrategy();

    int getAcknowledgeValue();
}
