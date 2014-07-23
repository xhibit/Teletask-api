package be.xhibit.teletask.model.nbt;

import be.xhibit.teletask.model.spec.Component;

public class Input {
    private final String id;
    private final String name;
    private final Component shortAction;
    private final Component longAction;

    public Input(String id, String name, Component shortAction, Component longAction) {
        this.id = id;
        this.name = name;
        this.shortAction = shortAction;
        this.longAction = longAction;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Component getShortAction() {
        return this.shortAction;
    }

    public Component getLongAction() {
        return this.longAction;
    }
}
