package be.xhibit.teletask.model.spec;

public interface Component {
    State getComponentState();

    void setComponentState(State state);

    Function getComponentFunction();

    int getComponentNumber();
}
