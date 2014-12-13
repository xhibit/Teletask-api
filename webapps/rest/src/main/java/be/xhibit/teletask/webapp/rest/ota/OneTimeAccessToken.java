package be.xhibit.teletask.webapp.rest.ota;

import be.xhibit.teletask.model.spec.Function;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as=OneTimeAccessToken.class)
public interface OneTimeAccessToken {
    String getToken();

    Function getFunction();

    int getNumber();

    String getState();

    boolean isValid();
}
