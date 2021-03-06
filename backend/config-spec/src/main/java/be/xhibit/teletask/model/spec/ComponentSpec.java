package be.xhibit.teletask.model.spec;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as=ComponentSpec.class)
public interface ComponentSpec {
    String getState();

    void setState(String state);

    Function getFunction();

    int getNumber();

    String getDescription();

    String getType();
}
