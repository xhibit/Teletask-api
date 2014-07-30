package be.xhibit.teletask.client.builder.composer;

import be.xhibit.teletask.client.builder.composer.config.configurables.CommandConfigurable;
import be.xhibit.teletask.client.builder.composer.config.configurables.FunctionConfigurable;
import be.xhibit.teletask.client.builder.composer.config.configurables.StateConfigurable;
import be.xhibit.teletask.client.builder.composer.config.configurables.StateKey;
import be.xhibit.teletask.client.builder.message.messages.MessageSupport;
import be.xhibit.teletask.client.builder.message.strategy.GroupGetStrategy;
import be.xhibit.teletask.client.builder.message.strategy.KeepAliveStrategy;
import be.xhibit.teletask.client.builder.message.messages.impl.EventMessage;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;

public interface MessageHandler {
    byte[] compose(Command command, byte[] payload);

    CommandConfigurable<?> getCommandConfig(Command command);

    StateConfigurable getStateConfig(State state);

    FunctionConfigurable getFunctionConfig(Function function);

    byte[] composeOutput(int... number);

    int getStxValue();

    EventMessage parseEvent(ClientConfigSpec config, byte[] message);

    Function getFunction(int function);

    Command getCommand(int command);

    State getState(StateKey key);

    boolean knows(Command command);

    boolean knows(Function function);

    boolean knows(State state);

    String getOutputLogHeaderName(int index);

    KeepAliveStrategy getKeepAliveStrategy();

    int getAcknowledgeValue();

    GroupGetStrategy getGroupGetStrategy();

    MessageSupport parse(ClientConfigSpec config, byte[] message);

    int getOutputByteSize();
}
