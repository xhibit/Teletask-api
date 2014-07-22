package be.xhibit.teletask.model.nbt;

import be.xhibit.teletask.model.spec.Component;
import be.xhibit.teletask.model.spec.State;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ComponentSupport implements Component {
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
    @JsonIgnore
    public State getComponentState() {
        return this.state;
    }

    @Override
    public void setComponentState(State state) {
        this.state = state;
    }

    public int getId() {
        return this.id;
    }

    @Override
    @JsonIgnore
    public int getComponentNumber() {
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
