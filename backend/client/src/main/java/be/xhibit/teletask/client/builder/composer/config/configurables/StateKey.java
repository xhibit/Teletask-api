package be.xhibit.teletask.client.builder.composer.config.configurables;

import be.xhibit.teletask.model.spec.Function;

public class StateKey {
    private final Function function;
    private final int value;

    public StateKey(Function function, int value) {
        this.function = function;
        this.value = value == -1 ? 255 : value;
    }

    public Function getFunction() {
        return this.function;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StateKey stateKey = (StateKey) o;

        if (value != stateKey.value) return false;
        if (function != stateKey.function) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = function != null ? function.hashCode() : 0;
        result = 31 * result + value;
        return result;
    }

    @Override
    public String toString() {
        return "StateKey{" +
                "function=" + function +
                ", value=" + value +
                '}';
    }
}
