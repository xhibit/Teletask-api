package be.xhibit.teletask.model.spec;

public class StateEnumImpl implements State {
    private final StateEnum state;
    private final Function function;

    public StateEnumImpl(StateEnum state, Function function) {
        this.state = state;
        this.function = function;
    }

    @Override
    public String getValue() {
        return this.state.name();
    }

    @Override
    public Function getFunction() {
        return this.function;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StateEnumImpl stateEnum = (StateEnumImpl) o;

        if (state != stateEnum.state) return false;

        return true;
    }

    public StateEnum getState() {
        return this.state;
    }

    @Override
    public int hashCode() {
        return state != null ? state.hashCode() : 0;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
