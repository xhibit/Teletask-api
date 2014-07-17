package be.xhibit.teletask.model.proprietary;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final String id;
    private final String name;
    private List<Relay> relays;

    public Room(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Relay> getRelays() {
        if (this.relays == null) {
            this.setRelays(new ArrayList<Relay>());
        }
        return this.relays;
    }

    private void setRelays(List<Relay> relays) {
        this.relays = relays;
    }
}
