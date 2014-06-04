package be.xhibit.teletask.api.model;

import be.xhibit.teletask.api.enums.Function;

import java.util.HashMap;
import java.util.List;

/**
 * POJO representation of a Room.  Holds all components which are controllable in that specific room.
 */
public class Room {
    private int id;
    private int level;
    private String name;
    private HashMap<Function, List<Integer>> components;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Function, List<Integer>> getComponents() {
        return components;
    }

    public void setComponents(HashMap<Function, List<Integer>> components) {
        this.components = components;
    }
}
