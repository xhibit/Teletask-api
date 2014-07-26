package be.xhibit.teletask.model.spec;

public class StateEnumImpl implements State {
    private final StateEnum state;

    public StateEnumImpl(StateEnum state) {
        this.state = state;
    }

    @Override
    public String getValue() {
        return this.state.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StateEnumImpl stateEnum = (StateEnumImpl) o;

        if (state != stateEnum.state) return false;

        return true;
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
