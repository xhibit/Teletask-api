package be.xhibit.teletask.client.builder.composer.config.sensor;

public class StateMapping {
    private final String name;
    private final Number number;

    public StateMapping(String name, Number number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    public Number getNumber() {
        return this.number;
    }
}
