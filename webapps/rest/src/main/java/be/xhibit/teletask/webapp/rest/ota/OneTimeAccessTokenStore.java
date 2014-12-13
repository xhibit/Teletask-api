package be.xhibit.teletask.webapp.rest.ota;

import be.xhibit.teletask.model.spec.Function;

public interface OneTimeAccessTokenStore {
    OneTimeAccessToken generate(Function function, int number, String state);

    OneTimeAccessToken get(String token);
}
