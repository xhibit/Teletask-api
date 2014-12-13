package be.xhibit.teletask.webapp.rest.ota;

import be.xhibit.teletask.model.spec.ComponentSpec;

public class OneTimeAccessExecutionResult {
    private final ComponentSpec componentSpec;
    private final String message;
    private final boolean success;

    public OneTimeAccessExecutionResult(ComponentSpec componentSpec, String message) {
        this.componentSpec = componentSpec;
        this.message = message;
        this.success = "OK".equals(message);
    }

    public ComponentSpec getComponentSpec() {
        return this.componentSpec;
    }

    public String getMessage() {
        return this.message;
    }
}
