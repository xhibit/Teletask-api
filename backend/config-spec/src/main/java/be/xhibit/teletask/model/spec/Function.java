package be.xhibit.teletask.model.spec;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Define all Teletask functions used by the API here.
 * <p/>
 * RELAY = 1; //control or get the status of a relay
 * DIMMER = 2; //control or get the status of a dimmer
 * MOTOR = 6; //control or get the status of a Motor: On/Off
 * LOCMOOD = 8; //control or get the status of a Local Mood
 * TIMEDMOOD = 9; //control or get the status of a Timed Local Mood
 * GENMOOD = 10; //control or get the status of a General Mood
 * FLAG = 15; //control or get the status of a Flag
 * SENSOR = 20; //get the status of a Sensor value
 * MTRUPDOWN = 55; //control or get the status of a Motor: Op/Down
 * COND = 60; //control or get the status of a Condition
 */
public enum Function {
    RELAY("relay", StateEnum.ON, StateEnum.OFF, StateEnum.TOGGLE),
    DIMMER("dimmer", DimmerStateImpl.DIMMER_STATES),
    MOTOR("motor on/off", StateEnum.UP, StateEnum.DOWN, StateEnum.STOP),
    LOCMOOD("local mood", StateEnum.ON, StateEnum.OFF, StateEnum.TOGGLE),
    TIMEDMOOD("timed mood", StateEnum.ON, StateEnum.OFF, StateEnum.TOGGLE),
    GENMOOD("general mood", StateEnum.ON, StateEnum.OFF, StateEnum.TOGGLE),
    FLAG("flag", StateEnum.ON, StateEnum.OFF, StateEnum.TOGGLE),
    SENSOR("sensor value", StateEnum.ON, StateEnum.OFF, StateEnum.TOGGLE),
    COND("condition", StateEnum.ON, StateEnum.OFF, StateEnum.TOGGLE);

    private final String descr;
    private final Map<String, State> states;

    Function(String descr, StateEnum... states) {
        this.descr = descr;
        ImmutableMap.Builder<String, State> builder = ImmutableMap.builder();
        for (StateEnum state : states) {
            builder.put(state.name().toUpperCase(), new StateEnumImpl(state, this));
        }
        this.states = builder.build();
    }

    Function(String descr, List<? extends State> states) {
        this.descr = descr;
        ImmutableMap.Builder<String, State> builder = ImmutableMap.builder();
        for (State state : states) {
            builder.put(state.getValue().toUpperCase(), state);
        }
        this.states = builder.build();
    }

    public String getDescription() {
        return this.descr;
    }

    public Collection<State> getStates() {
        return this.states.values();
    }

    public State stateValue(String state) {
        return this.states.get(state.toUpperCase());
    }
}
