package be.xhibit.teletask.webapp;

import be.xhibit.teletask.client.TDSClient;
import be.xhibit.teletask.config.model.json.TDSClientConfig;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.parser.FullNbtModelConsumerImpl;
import be.xhibit.teletask.parser.PrintedFileVisitor;
import be.xhibit.teletask.webapp.rest.TeletaskHttpServletDispatcher;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;

public final class ClientHolder {
    /**
     * Logger responsible for logging and debugging statements.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ClientHolder.class);

    private static TDSClient client;

    private ClientHolder() {
    }

    public static void setClient(TDSClient client) {
        ClientHolder.client = client;
    }

    public static TDSClient getClient() {
        if (client == null) {
            setClient(TDSClient.getInstance(getClientConfig()));
        }
        return client;
    }

    protected static ClientConfigSpec getClientConfig() {
        ClientConfigSpec clientConfig = null;

        String configFile = System.getProperty("configFile");

        Preconditions.checkArgument(configFile != null, "Please specify -DconfigFile as a startup parameter");

        LOG.debug("Using config file {}", configFile);

        try (FileInputStream jsonData = new FileInputStream(configFile)) {
            LOG.debug("Trying to load the configFile as json...");
            clientConfig = TDSClientConfig.read(jsonData);
        } catch (Exception e) {
            LOG.debug("Trying to load the configFile as nbt printed text...");
            try (FileInputStream inputStream = new FileInputStream(configFile)) {
                FullNbtModelConsumerImpl consumer = new FullNbtModelConsumerImpl();
                PrintedFileVisitor.getInstance().visit(consumer, inputStream);
                clientConfig = consumer.getCentralUnit();
            } catch (IOException e1) {
                LOG.error("Exception ({}) caught in getClientConfig: {}", e1.getClass().getName(), e1.getMessage(), e1);
            }
        }

        Preconditions.checkState(clientConfig != null, "Could not determine the configuration. Make sure the clientConfig parameter refers to a valid json config file or a valid teletask nbt printed text file.");

        return clientConfig;
    }
}
