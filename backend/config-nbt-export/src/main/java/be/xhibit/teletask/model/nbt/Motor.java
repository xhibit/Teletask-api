package be.xhibit.teletask.model.nbt;

import be.xhibit.teletask.model.spec.Function;

public class Motor extends ComponentSupport {
    public Motor(int id, Room room, String type, String description) {
        super(id, room, description, type);
    }

    @Override
    public Function getFunctionValue() {
        return Function.MOTOR;
    }
}
