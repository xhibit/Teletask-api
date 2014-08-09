package be.xhibit.teletask.client.builder.composer.v2_8;

public enum MicrosLogState {
    ON(255),
    OFF(0);

    private final byte byteValue;

    MicrosLogState(int byteValue) {
        this.byteValue = (byte) byteValue;
    }

    public byte getByteValue() {
        return this.byteValue;
    }
}
