package be.xhibit.teletask.client.builder;

public enum SendResult {
    SUCCESS(1),
    FAILED(0),
    INVALID(2),
    UNKNOW_COMMAND(3);

    private final int code;

    SendResult(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
