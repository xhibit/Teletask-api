package be.xhibit.teletask.api.model;

/**
 * The APIResponse class used in this API.
 */
public class APIResponse {
    private String status;
    private TDSComponent component;

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
    public APIResponse(String status, TDSComponent component) {
        this.status = status;
        this.component = component;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TDSComponent getComponent() {
        return component;
    }

    public void setComponent(TDSComponent component) {
        this.component = component;
    }
}
