package be.xhibit.teletask.model.nbt;

import be.xhibit.teletask.model.spec.ComponentSpec;
import be.xhibit.teletask.model.spec.RoomSpec;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

public class Room implements RoomSpec {
    private final int id;
    private final String name;
    private List<Relay> relays;

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
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
