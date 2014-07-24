package be.xhibit.teletask.model.nbt;

import be.xhibit.teletask.model.spec.ComponentSpec;
import be.xhibit.teletask.model.spec.State;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ComponentSupport implements ComponentSpec {
    protected final int id;
    protected final Room room;
    protected final String description;
    protected final String type;
    private State state;

    public ComponentSupport(int id, Room room, String description, String type) {
        this.id = id;
        this.room = room;
        this.description = description;
        this.type = type;
    }

    @Override
    public State getStateValue() {
        return this.state;
    }

    @Override
    public void setStateValue(State state) {
        this.state = state;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public int getNumber() {
        return this.getId();
    }

    @JsonIgnore
    public Room getRoom() {
        return this.room;
    }

    public int getRoomId() {
        return this.getRoom().getId();
    }

    public String getDescription() {
        return this.description;
    }

    public String getType() {
        return this.type;
    }
}
