package be.xhibit.teletask.client.builder.composer.config;

public abstract class Configurable<T> {
    private final int number;
    private final T object;

    public Configurable(int number, T object) {
        this.number = number;
        this.object = object;
    }

    public T getObject() {
        return this.object;
    }

    public int getNumber() {
        return this.number;
    }
}
