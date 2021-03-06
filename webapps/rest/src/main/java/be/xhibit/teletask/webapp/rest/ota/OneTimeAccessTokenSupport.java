package be.xhibit.teletask.webapp.rest.ota;

import be.xhibit.teletask.model.spec.Function;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;

public abstract class OneTimeAccessTokenSupport implements OneTimeAccessToken {
    private final String token;
    private final Function function;
    private final int number;
    private final String state;

    public OneTimeAccessTokenSupport(Function function, int number, String state) {
        this.number = number;
        this.function = function;
        this.state = state;
        this.token = new UUIDGenerator().generateId(this).toString();
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public Function getFunction() {
        return this.function;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

    @Override
    public String getState() {
        return this.state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        OneTimeAccessTokenSupport that = (OneTimeAccessTokenSupport) o;

        return this.token.equals(that.token);

    }

    @Override
    public int hashCode() {
        return this.token.hashCode();
    }

    @Override
    public String toString() {
        return "OneTimeAccessTokenSupport{" +
                "token='" + this.getToken() + '\'' +
                ", function=" + this.getFunction() +
                ", number=" + this.getNumber() +
                ", state='" + this.getState() + '\'' +
                '}';
    }
}
