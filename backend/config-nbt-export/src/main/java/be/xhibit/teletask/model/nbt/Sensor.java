package be.xhibit.teletask.model.nbt;

import be.xhibit.teletask.model.spec.Function;

public class Sensor extends ComponentSupport {
    public Sensor(int id, Room room, String description) {
        super(id, room, "Sensor", description);
    }

    @Override
    public Function getFunction() {
        return Function.SENSOR;
    }
}
