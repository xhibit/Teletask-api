package be.xhibit.teletask.model.proprietary;

public class Relay {
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

    public Room getRoom() {
        return this.room;
    }

    public String getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }
}
