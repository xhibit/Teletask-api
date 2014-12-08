package be.xhibit.teletask.webapp.rest.ota;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as = TimedOneTimeAccessToken.class)
public interface TimedOneTimeAccessToken extends OneTimeAccessToken {
    String getValidTime();
}
