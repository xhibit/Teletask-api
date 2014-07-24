package be.xhibit.teletask.model.spec;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as=ComponentSpec.class)
public interface ComponentSpec {
    State getStateValue();

    void setStateValue(State state);

    Function getFunctionValue();

    int getNumber();
}
