package be.xhibit.teletask.config.model.json;

import be.xhibit.teletask.model.spec.Function;

import java.util.HashMap;
import java.util.List;

/**
 * POJO representation of a Room.  Holds all components which are controllable in that specific room.
 */
public class Room {
    private int id;
    private int level;
    private String name;
    private HashMap<Function, List<TDSComponent>> components;
    private HashMap<Function, List<Integer>> componentTypes;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Function, List<TDSComponent>> getComponents() {
        if (this.components == null) {
            this.components = new HashMap<>();
        }
        return this.components;
    }

    public void setComponents(HashMap<Function, List<TDSComponent>> components) {
        this.components = components;
    }

    public HashMap<Function, List<Integer>> getComponentTypes() {
        return this.componentTypes;
    }

    public void setComponentTypes(HashMap<Function, List<Integer>> componentTypes) {
        this.componentTypes = componentTypes;
    }
}
