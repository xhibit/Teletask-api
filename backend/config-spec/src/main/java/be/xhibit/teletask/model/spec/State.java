package be.xhibit.teletask.model.spec;

public enum State {
    ON(255),
    OFF(0),
    EVENT(1),
    UP(255),
    DOWN(0);

    private final byte code;

    State(int code) {
        this.code = (byte) code;
    }

    public byte getCode() {
        return this.code;
    }
}
