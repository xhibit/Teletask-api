package be.xhibit.teletask.webapp.rest.ota;

import be.xhibit.teletask.model.spec.Function;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.concurrent.TimeUnit;

public class TimedOneTimeAccessTokenImpl extends OneTimeAccessTokenSupport implements TimedOneTimeAccessToken {
    private final LocalDateTime validUntil;

    public TimedOneTimeAccessTokenImpl(Function function, int number, String state) {
        super(function, number, state);
        String minutes = System.getProperty("ota.minutes", String.valueOf(TimeUnit.HOURS.toMinutes(8)));
        this.validUntil = LocalDateTime.now().plusMinutes(Integer.valueOf(minutes));
    }

    public LocalDateTime getValidUntil() {
        return this.validUntil;
    }

    @Override
    public String getValidTime() {
        return ISODateTimeFormat.dateHourMinute().print(this.getValidUntil());
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
