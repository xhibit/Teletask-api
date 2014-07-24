package be.xhibit.teletask.model.spec;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;

import java.util.HashMap;
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
    RELAY("relay", State.ON, State.OFF, State.TOGGLE),
    DIMMER("dimmer", State.ON, State.OFF, State.TOGGLE),
    MOTOR("motor on/off", State.UP, State.DOWN, State.STOP),
    LOCMOOD("local mood", State.ON, State.OFF, State.TOGGLE),
    TIMEDMOOD("timed mood", State.ON, State.OFF, State.TOGGLE),
    GENMOOD("general mood", State.ON, State.OFF, State.TOGGLE),
    FLAG("flag", State.ON, State.OFF, State.TOGGLE),
    SENSOR("sensor value", State.ON, State.OFF, State.TOGGLE),
    COND("condition", State.ON, State.OFF, State.TOGGLE);

    private final String descr;
    private final List<State> states;

    Function(String descr, State... states) {
        this.descr = descr;
        ImmutableList.Builder<State> builder = ImmutableList.builder();
        for (State state : states) {
            builder.add(state);
        }
        this.states = builder.build();
    }

    public String getDescription() {
        return this.descr;
    }

    public List<State> getStates() {
        return this.states;
    }
}
