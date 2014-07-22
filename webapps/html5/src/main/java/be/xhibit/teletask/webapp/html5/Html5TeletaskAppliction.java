package be.xhibit.teletask.webapp.html5;

import be.xhibit.teletask.config.model.json.TDSClientConfig;
import be.xhibit.teletask.model.spec.ClientConfig;
import be.xhibit.teletask.webapp.rest.TeletaskApplication;

public class Html5TeletaskAppliction extends TeletaskApplication {
    private ClientConfig clientConfig;

    @Override
    protected ClientConfig getClientConfig() {
        if (this.clientConfig == null) {
            this.clientConfig = TDSClientConfig.read(TDSClientConfig.class.getClassLoader().getResourceAsStream("tds-config.json"));
        }
        return this.clientConfig;
    }
}