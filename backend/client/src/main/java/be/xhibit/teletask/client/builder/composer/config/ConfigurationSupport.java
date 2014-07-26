package be.xhibit.teletask.client.builder.composer.config;

import be.xhibit.teletask.model.spec.Command;

import java.util.HashMap;
import java.util.Map;

public abstract class ConfigurationSupport<T, C extends Configurable<T>> {
    private Map<Integer, C> configByNumber;
    private Map<T, C> configByObject;
    private final Iterable<C> config;

    public ConfigurationSupport(Iterable<C> config) {
        this.config = config;
    }

    public T getConfigObject(int number) {
        return this.getConfigByNumber().get(number).getObject();
    }

    public C getConfigurable(T configObject) {
        return this.getConfigByObject().get(configObject);
    }

    private Map<Integer, C> getConfigByNumber() {
        if (this.configByNumber == null) {
            this.configByNumber = this.createConfigByNumberMap(this.getConfig());
        }
        return this.configByNumber;
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

    private Map<Integer, C> createConfigByNumberMap(Iterable<C> map) {
        Map<Integer, C> configMap = new HashMap<>();
        for (C value : map) {
            configMap.put(value.getNumber(), value);
        }
        return configMap;
    }

    private Iterable<C> getConfig() {
        return this.config;
    }

    public boolean knows(T command) {
        return this.getConfigByObject().containsKey(command);
    }
}
