package be.xhibit.teletask.model.spec;

import com.google.common.primitives.Ints;

import java.util.HashMap;
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
 * GETSENSVAL = 20; //get the status of a Sensor value
 * MTRUPDOWN = 55; //control or get the status of a Motor: Op/Down
 * COND = 60; //control or get the status of a Condition
 */
public enum Function {
    RELAY(1, "relay", State.ON, State.OFF, State.TOGGLE),
    DIMMER(2, "dimmer", State.ON, State.OFF, State.TOGGLE),
    MOTOR(6, "motor on/off", State.ON, State.OFF, State.TOGGLE),
    LOCMOOD(8, "local mood", State.ON, State.OFF, State.TOGGLE),
    TIMEDMOOD(9, "timed mood", State.ON, State.OFF, State.TOGGLE),
    GENMOOD(10, "general mood", State.ON, State.OFF, State.TOGGLE),
    FLAG(15, "flag", State.ON, State.OFF, State.TOGGLE),
    GETSENSVAL(25, "sensor value", State.ON, State.OFF, State.TOGGLE),
    MTRUPDOWN(55, "motor up/down", State.UP, State.DOWN),
    COND(60, "condition", State.ON, State.OFF, State.TOGGLE);

    private final byte code;
    private final String descr;
    private final Map<Integer, State> states = new HashMap<>();

    Function(int code, String descr, State... states) {
        this.code = (byte) code;
        this.descr = descr;
        for (State state : states) {
            this.states.put(state.getCode(), state);
        }
    }

    public String getDescription() {
        return this.descr;
    }

    public byte getCode() {
        return this.code;
    }

    public State getState(int code) {
        return this.states.get(code);
    }

    public State getState(String code) {
        Integer stateCode = Ints.tryParse(code);
        return stateCode == null ? State.valueOf(code.toUpperCase()) : this.states.get(stateCode);
    }

    private static final Map<Integer, Function> map = new HashMap<Integer, Function>();

    static {
        for (Function function : Function.values()) {
            map.put((int) function.code, function);
        }
    }

    public static Function valueOf(int code) {
        return map.get(code);
    }
}
