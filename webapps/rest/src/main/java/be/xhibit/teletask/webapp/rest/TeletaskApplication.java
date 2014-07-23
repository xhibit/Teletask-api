package be.xhibit.teletask.webapp.rest;

import be.xhibit.teletask.config.model.json.TDSClientConfig;
import be.xhibit.teletask.model.spec.ClientConfig;
import be.xhibit.teletask.parser.FullNbtModelConsumerImpl;
import be.xhibit.teletask.parser.PrintedFileVisitor;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Defines the components of a JAX-RS application and supplies additional metadata.
 */
public class TeletaskApplication extends Application {
    /**
     * Logger responsible for logging and debugging statements.
     */
    private static final Logger LOG = LoggerFactory.getLogger(TeletaskApplication.class);

    private final Set<Object> singletons = new HashSet<Object>();

    public TeletaskApplication() {
        this.singletons.add(new ComponentResource(this.getClientConfig()));
    }

    /**
     * <p>Get a set of root resource and provider instances. Fields and properties
     * of returned instances are injected with their declared dependencies
     * (see {@link Context}) by the runtime prior to use.
     * <p/>
     * <p>Implementations should warn about and ignore classes that do not
     * conform to the requirements of root resource or provider classes.
     * Implementations should flag an error if the returned set includes
     * more than one instance of the same class. Implementations MUST
     * NOT modify the returned set.</p>
     * <p/>
     * <p>The default implementation returns an empty set.</p>
     *
     * @return a set of root resource and provider instances. Returning null
     * is equivalent to returning an empty set.
     */
    @Override
    public Set<Object> getSingletons() {
        return this.singletons;
    }

    protected ClientConfig getClientConfig() {
        ClientConfig clientConfig = null;

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
