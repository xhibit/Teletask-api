package be.xhibit.teletask.webapp.rest;

import be.xhibit.teletask.model.spec.Component;

/**
 * The APIResponse class used in this API.
 */
public class APIResponse {
    private String status;
    private Component component;

    /**
     * Default constructor.
     */
    public APIResponse() {
    }

    /**
     * Constructor setting both the response status and the component status.
     * @param status The HTTP REST response status. Indicated whether the REST request was either "success" or "failed".
     *               Can be useful in interpreting the result for proper error handling.
     * @param component The TDS Component status.
     */
    public APIResponse(String status, Component component) {
        this.status = status;
        this.component = component;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}
