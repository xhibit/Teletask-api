package be.xhibit.teletask.model.nbt;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Relay implements Action {
    private final String id;
    private final Room room;
    private final String type;
    private final String description;

    public Relay(String id, Room room, String type, String description) {
        this.id = id;
        this.room = room;
        this.type = type;
        this.description = description;
    }

    public String getId() {
        return this.id;
    }

    @JsonIgnore
    public Room getRoom() {
        return this.room;
    }

    public String getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String getActionType() {
        return "REL";
    }

    public String getRoomId() {
        return this.getRoom().getId();
    }

    @Override
    public void execute() {

    }
}
