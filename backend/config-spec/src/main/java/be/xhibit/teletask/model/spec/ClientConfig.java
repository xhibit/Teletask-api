package be.xhibit.teletask.model.spec;

public interface ClientConfig {

    String getHost();

    int getPort();

    Component getComponent(Function function, int number);
}
