package be.xhibit.teletask.webapp.rest;

import be.xhibit.teletask.model.spec.Component;

/**
 * The APIResponse class used in this API.
 */
public class APIResponse {
    private String status;
    private APIComponent component;

    /**
     * Default constructor.
     */
    public APIResponse() {
    }

    /**
     * Constructor setting both the response status and the component status.
     *
     * @param status    The HTTP REST response status. Indicated whether the REST request was either "success" or "failed".
     *                  Can be useful in interpreting the result for proper error handling.
     * @param component The TDS Component status.
     */
    public APIResponse(String status, Component component) {
        this.status = status;
        this.component = new APIComponent(component);
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public APIComponent getComponent() {
        return this.component;
    }

    public void setComponent(Component component) {
        this.component = new APIComponent(component);
    }

    public void setComponent(APIComponent component) {
        this.component = component;
    }

    private static class APIComponent {
        private final Component component;

        private APIComponent(Component component) {
            this.component = component;
        }

        public int getFunction() {
            return this.component.getComponentFunction().getCode();
        }

        public int getNumber() {
            return this.component.getComponentNumber();
        }

        public int getState() {
            return this.component.getComponentState().getCode();
        }
    }
}
