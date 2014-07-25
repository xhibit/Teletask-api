package be.xhibit.teletask.client.builder.composer;

import be.xhibit.teletask.client.builder.CommandConfig;
import be.xhibit.teletask.client.builder.ConfigSupport;
import be.xhibit.teletask.client.builder.FunctionConfig;
import be.xhibit.teletask.client.builder.StateConfig;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public abstract class MessageHandlerSupport implements MessageHandler {
    private final Map<Command, CommandConfig> commandConfigMap;
    private final Map<State, StateConfig> stateConfigMap;
    private final Map<Function, FunctionConfig> functionConfigMap;

    private Map<Integer, Command> commandMap;
    private Map<Integer, State> stateMap;
    private Map<Integer, Function> functionMap;

    protected MessageHandlerSupport(Map<Command, CommandConfig> commandConfigMap, Map<State, StateConfig> stateConfigMap, Map<Function, FunctionConfig> functionConfigMap) {
        this.commandConfigMap = commandConfigMap;
        this.stateConfigMap = stateConfigMap;
        this.functionConfigMap = functionConfigMap;
    }

    private String getMessageChecksumCalculationSteps(byte[] message) {
        StringBuilder builder = new StringBuilder(100);
        boolean first = true;
        for (byte messageByte : message) {
            if (!first) {
                builder.append(" + ");
            }
            builder.append(messageByte);
            first = false;
        }
        return builder.toString();
    }

    protected byte[] getMessageWithChecksum(byte[] messageBytes) {
        // ChkSm: Command Number + Command Parameters + Length + STX
        byte checkSumByte = 0;
        for (byte messageByte : messageBytes) {
            checkSumByte += messageByte;
        }
        this.getLogger().debug("Checksum calculation: {} = {}", this.getMessageChecksumCalculationSteps(messageBytes), checkSumByte);
        messageBytes = Bytes.concat(messageBytes, new byte[]{checkSumByte});
        return messageBytes;
    }

    @Override
    public int getStxValue() {
        return 2;
    }

    protected abstract Logger getLogger();

    protected Map<Integer, Command> getCommandMap() {
        if (this.commandMap == null) {
            this.commandMap = this.createConfigMap(this.getCommandConfig());
        }
        return this.commandMap;
    }

    protected Map<Integer, State> getStateMap() {
        if (this.stateMap == null) {
            this.stateMap = this.createConfigMap(this.getStateConfig());
        }
        return this.stateMap;
    }

    protected Map<Integer, Function> getFunctionMap() {
        if (this.functionMap == null) {
            this.functionMap = this.createConfigMap(this.getFunctionConfig());
        }
        return this.functionMap;
    }

    private <T extends ConfigSupport, E> Map<Integer, E> createConfigMap(Map<E, T> map) {
        Map<Integer, E> configMap = new HashMap<>();
        for (Map.Entry<E, T> entry : map.entrySet()) {
            configMap.put(entry.getValue().getNumber(), entry.getKey());
        }
        return configMap;
    }

    private Map<Command, CommandConfig> getCommandConfig() {
        return this.commandConfigMap;
    }

    private Map<State, StateConfig> getStateConfig() {
        return this.stateConfigMap;
    }

    private Map<Function, FunctionConfig> getFunctionConfig() {
        return this.functionConfigMap;
    }

    @Override
    public Command getCommand(int command) {
        return this.getCommandMap().get(command);
    }

    @Override
    public State getState(int state) {
        return this.getStateMap().get(state);
    }

    @Override
    public boolean knowsCommand(Command command) {
        return this.commandConfigMap.containsKey(command);
    }

    @Override
    public String getOutputLogHeaderName(int index) {
        return "Output";
    }

    @Override
    public int getAcknowledgeValue() {
        return 10;
    }

    @Override
    public Function getFunction(int function) {
        return this.getFunctionMap().get(function);
    }

    @Override
    public CommandConfig getCommandConfig(Command command) {
        return this.getCommandConfig().get(command);
    }

    @Override
    public StateConfig getStateConfig(State state) {
        return this.getStateConfig().get(state);
    }

    @Override
    public FunctionConfig getFunctionConfig(Function function) {
        return this.getFunctionConfig().get(function);
    }
}
