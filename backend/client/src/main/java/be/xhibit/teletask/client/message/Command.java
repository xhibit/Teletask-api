package be.xhibit.teletask.client.message;

public enum Command {
    SET(1),
    GET(2),
    LOG(3);

    private final int code;

    Command(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
