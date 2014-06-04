package be.xhibit.teletask.api.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Define all Teletask functions used by the API here.
 *
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
    RELAY(1, "relay"),
    DIMMER(2, "dimmer"),
    MOTOR(6, "motor on/off"),
    LOCMOOD(8, "local mood"),
    TIMEDMOOD(9, "timed mood"),
    GENMOOD(10, "general mood"),
    FLAG(15, "flag"),
    GETSENSVAL(25, "sensor value"),
    MTRUPDOWN(55, "motor up/down"),
    COND(60, "condition");

    private int code;
    private String descr;

    private Function(int code, String descr) {
        this.code = code;
        this.descr = descr;
    }

    public String getDescription() {
        return descr;
    }

    public int getCode() {
        return code;
    }

    private static Map<Integer, Function> map = new HashMap<Integer, Function>();

    static {
        for (Function function : Function.values()) {
            map.put(function.code, function);
        }
    }

    public static Function valueOf(int code) {
        return map.get(code);
    }

    /*@Override
    public String toString() {
        return this.getDescription();
    }*/
}
