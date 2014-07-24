package be.xhibit.teletask.model.spec;

public enum State {
    ON(255),
    OFF(0),
    EVENT(1),
    UP(255),
    TOGGLE(103),
    DOWN(0);

    private final int code;

    State(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
