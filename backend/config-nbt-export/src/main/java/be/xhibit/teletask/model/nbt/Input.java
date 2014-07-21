package be.xhibit.teletask.model.nbt;

public class Input {
    private final String id;
    private final String name;
    private final Action shortAction;
    private final Action longAction;

    public Input(String id, String name, Action shortAction, Action longAction) {
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

    public Action getShortAction() {
        return this.shortAction;
    }

    public Action getLongAction() {
        return this.longAction;
    }
}
