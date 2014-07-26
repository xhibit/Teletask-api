package be.xhibit.teletask.webapp.rest;

import be.xhibit.teletask.model.spec.ComponentSpec;

/**
 * The APIResponse class used in this API.
 */
public class APIResponse {
    private final String status;
    private final ComponentSpec[] component;

    /**
     * Constructor setting both the response status and the component status.
     *
     * @param status    The HTTP REST response status. Indicated whether the REST request was either "success" or "failed".
     *                  Can be useful in interpreting the result for proper error handling.
     * @param component The TDS Component status.
     */
    public APIResponse(String status, ComponentSpec... component) {
        this.status = status;
        this.component = component;
    }

    public String getStatus() {
        return this.status;
    }

    public ComponentSpec[] getComponent() {
        return this.component;
    }
}
