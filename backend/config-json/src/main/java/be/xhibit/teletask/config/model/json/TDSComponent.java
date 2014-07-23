package be.xhibit.teletask.config.model.json;

import be.xhibit.teletask.model.spec.Component;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;

/**
 * This class represents a Teletask component, being either a: relay, motor, mood, ... basically anything which can be controlled.
 */
public class TDSComponent implements Component {
    private String description;
    private int function;
    private int number;
    private int state;

    /**
     * Default constructor.
     * The default constructor is used by Jackson.  In order not to have null values, some fields are initialised to empty strings.
     */
    public TDSComponent() {
        this.description = "";
        this.state = 0;
    }

    /**
     * Constructor taking arguments status, state and number.
     * @param function The function for which the call was requested.
     * @param state The current status of the component, for example 0 indicating off for a "relay".
     * @param number The component number you wish to manipulate.
     */
    public TDSComponent(int function, int state, int number) {
        this.function = function;
        this.state = state;
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getFunction() {
        return this.function;
    }

    public void setFunction(int function) {
        this.function = function;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public State getComponentState() {
        return this.getComponentFunction().getState(this.getState());
    }

    @Override
    public void setComponentState(State state) {
        this.setState(state.getCode());
    }

    @Override
    public Function getComponentFunction() {
        return Function.valueOf(this.getFunction());
    }

    @Override
    public int getComponentNumber() {
        return this.getNumber();
    }
}
