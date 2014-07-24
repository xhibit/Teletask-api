package be.xhibit.teletask.model.nbt;

import be.xhibit.teletask.model.spec.RoomSpec;

import java.util.ArrayList;
import java.util.List;

public class Room implements RoomSpec {
    private final int id;
    private final String name;
    private List<Relay> relays;
    private List<LocalMood> localMoods;
    private List<Motor> motors;

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

    @Override
    public List<LocalMood> getLocalMoods() {
        if (this.localMoods == null) {
            this.setLocalMoods(new ArrayList<LocalMood>());
        }
        return this.localMoods;
    }

    @Override
    public List<Motor> getMotors() {
        if (this.motors == null) {
            this.setMotors(new ArrayList<Motor>());
        }
        return this.motors;
    }

    private void setMotors(List<Motor> motors) {
        this.motors = motors;
    }

    private void setLocalMoods(List<LocalMood> localMoods) {
        this.localMoods = localMoods;
    }

    private void setRelays(List<Relay> relays) {
        this.relays = relays;
    }
}
