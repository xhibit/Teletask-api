package be.xhibit.teletask.webapp.rest.ota;

import be.xhibit.teletask.model.spec.Function;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class OneTimeAccessTokenStoreMemoryImpl implements OneTimeAccessTokenStore {
    private final Map<String, OneTimeAccessTokenSupport> tokens = new ConcurrentHashMap<>();

    public OneTimeAccessTokenStoreMemoryImpl() {
        new Timer().schedule(new RemoveInvalidTokensTask(), 0, TimeUnit.HOURS.toMillis(1));
    }

    @Override
    public OneTimeAccessToken generate(Function function, int number, String state) {
        OneTimeAccessTokenSupport token = new TimedOneTimeAccessToken(function, number, state);

        this.getTokens().put(token.getToken(), token);

        return token;
    }

    @Override
    public OneTimeAccessToken get(String token) {
        OneTimeAccessTokenSupport oneTimeAccessToken = this.getTokens().get(token);
        if (oneTimeAccessToken != null) {
            this.getTokens().remove(oneTimeAccessToken.getToken());
        }
        return oneTimeAccessToken;
    }

    public Map<String, OneTimeAccessTokenSupport> getTokens() {
        return this.tokens;
    }

    private class RemoveInvalidTokensTask extends TimerTask {
        @Override
        public void run() {
            for (Entry<String, OneTimeAccessTokenSupport> entry : OneTimeAccessTokenStoreMemoryImpl.this.tokens.entrySet()) {
                if (!entry.getValue().isValid()) {
                    OneTimeAccessTokenStoreMemoryImpl.this.tokens.remove(entry.getKey());
                }
            }
        }
    }
}
