package be.xhibit.teletask.config.model.json;

import be.xhibit.teletask.model.spec.function.Function;
import be.xhibit.teletask.model.spec.function.FunctionExecution;
import be.xhibit.teletask.model.spec.function.FunctionExecutionContext;
import be.xhibit.teletask.model.spec.function.FunctionExecutionPayload;

/**
 * This class represents a Teletask component, being either a: relay, motor, mood, ... basically anything which can be controlled.
 */
public class TDSComponent implements FunctionExecution<FunctionExecutionContext, FunctionExecutionPayload> {
    private String description;
    private Function function;
    private int number;
    private int state;

    /**
     * Default constructor.
     * The default constructor is used by Jackson.  In order not to have null values, some fields are initialised to empty strings.
     */
    public TDSComponent() {
        description = "";
        state = 0;
    }

    /**
     * Constructor taking arguments status, state and number.
     * @param function The function for which the call was requested.
     * @param state The current status of the component, for example 0 indicating off for a "relay".
     * @param number The component number you wish to manipulate.
     */
    public TDSComponent(Function function, int state, int number) {
        this.function = function;
        this.state = state;
        this.number = number;
    }

    @Override
    public FunctionExecutionPayload createPayload(FunctionExecutionContext context) {
        return null;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
