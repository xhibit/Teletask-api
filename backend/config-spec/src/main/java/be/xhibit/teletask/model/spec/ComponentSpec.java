package be.xhibit.teletask.model.spec;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as=ComponentSpec.class)
public interface ComponentSpec {
    State getState();

    void setState(State state);

    Function getFunction();

    int getNumber();

    String getDescription();
}
