package be.xhibit.teletask.webapp.rest.ota;

import be.xhibit.teletask.model.spec.Function;
import org.joda.time.LocalDateTime;

import java.util.concurrent.TimeUnit;

public class TimedOneTimeAccessToken extends OneTimeAccessTokenSupport {
    private final LocalDateTime validUntil;

    public TimedOneTimeAccessToken(Function function, int number, String state) {
        super(function, number, state);
        String minutes = System.getProperty("ota.minutes", String.valueOf(TimeUnit.HOURS.toMinutes(8)));
        this.validUntil = LocalDateTime.now().plusMinutes(Integer.valueOf(minutes));
    }

    public LocalDateTime getValidUntil() {
        return this.validUntil;
    }

    @Override
    public boolean isValid() {
        return LocalDateTime.now().isBefore(this.getValidUntil());
    }

    @Override
    public String toString() {
        return "TimedOneTimeAccessToken{" +
                "token='" + this.getToken() + '\'' +
                ", function=" + this.getFunction() +
                ", number=" + this.getNumber() +
                ", state='" + this.getState() + '\'' +
                ", validUntil=" + this.getValidUntil() +
                '}';
    }
}
