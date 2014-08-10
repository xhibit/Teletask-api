package be.xhibit.teletask.client.builder.composer.config;

import be.xhibit.teletask.client.builder.composer.MessageHandler;
import be.xhibit.teletask.client.builder.composer.config.statecalculator.StateCalculator;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.ComponentSpec;
import be.xhibit.teletask.model.spec.Function;

import java.util.HashMap;
import java.util.Map;

public abstract class ConfigurationSupport<T, C extends Configurable<T>, K> {
    private Map<K, C> configByKey;
    private Map<T, C> configByObject;
    private final Iterable<C> config;

    public ConfigurationSupport(Iterable<C> config) {
        this.config = config;
    }

    protected static String getState(MessageHandler messageHandler, ClientConfigSpec config, Function function, int number, byte[] payload, int startIndex) {
        ComponentSpec component = config.getComponent(function, number);
        StateCalculator stateCalculator = messageHandler.getFunctionConfig(function).getStateCalculator();
        byte[] stateBytes = stateCalculator.getNumberConverter().read(payload, startIndex);
        return stateCalculator.convertGet(component, stateBytes);
    }

    public T getConfigObject(K key) {
        C configObject = this.getConfigByKey().get(key);

        if (configObject == null) {
            throw new IllegalStateException("Configuration " + this.getClass().getSimpleName() + " not found for key " + key);
        }

        return configObject.getObject();
    }

    public C getConfigurable(T configObject) {
        C state = this.getConfigByObject().get(configObject);
        if (state == null) {
            throw new IllegalStateException("Configuration " + this.getClass().getSimpleName() + " not found for configObject " + configObject);
        }
        return state;
    }

    private Map<K, C> getConfigByKey() {
        if (this.configByKey == null) {
            this.configByKey = this.createConfigByKeyMap(this.getConfig());
        }
        return this.configByKey;
    }

    private Map<T, C> getConfigByObject() {
        if (this.configByObject == null) {
            this.configByObject = this.createConfigByObjectMap(this.getConfig());
        }
        return this.configByObject;
    }

    private Map<T, C> createConfigByObjectMap(Iterable<C> map) {
        Map<T, C> configMap = new HashMap<>();
        for (C value : map) {
            configMap.put(value.getObject(), value);
        }
        return configMap;
    }

    private Map<K, C> createConfigByKeyMap(Iterable<C> map) {
        Map<K, C> configMap = new HashMap<>();
        for (C value : map) {
            configMap.put(this.getKey(value), value);
        }
        return configMap;
    }

    protected abstract K getKey(C configurable);

    private Iterable<C> getConfig() {
        return this.config;
    }

    public boolean knows(T command) {
        return this.getConfigByObject().containsKey(command);
    }
}
