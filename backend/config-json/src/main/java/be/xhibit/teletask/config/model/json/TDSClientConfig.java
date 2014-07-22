package be.xhibit.teletask.config.model.json;

import be.xhibit.teletask.model.spec.CentralUnitType;
import be.xhibit.teletask.model.spec.ClientConfig;
import be.xhibit.teletask.model.spec.Function;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * POJO representation of the TDS config JSON file.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class TDSClientConfig implements ClientConfig {
    /**
     * LOG responsible for logging and debugging statements.
     */
    static final Logger LOG = LogManager.getLogger(TDSClientConfig.class.getName());

    private String host;
    private int port;
    private boolean testMode;
    private Map<Function, List<TDSComponent>> componentsTypes;
    private List<Room> rooms;

    /**
     * Default constructor.
     */
    private TDSClientConfig() {
    }

    public TDSClientConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isTestMode() {
        return this.testMode;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public Map<Function, List<TDSComponent>> getComponentsTypes() {
        return this.componentsTypes;
    }

    public void setComponentsTypes(Map<Function, List<TDSComponent>> componentsTypes) {
        this.componentsTypes = componentsTypes;
    }

    public List<Room> getRooms() {
        return this.rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public CentralUnitType getCentralUnitType() {
        //TODO: Get from json config file
        return CentralUnitType.MICROS;
    }

// ================================ HELPER METHODS

    @Override
    public TDSComponent getComponent(Function function, int number) {
        TDSComponent returnValue = null;

        List<TDSComponent> components = this.componentsTypes.get(function);

        //components.get()  ///TODO: refactor: access by index not OK, should be based on number, therefore iterate, or store as HashMap.
        for (TDSComponent component : components) {
            if (component.getNumber() == number) {
                component.setFunction(function.getCode());
                returnValue = component;
                break;
            }
        }

        return returnValue;
    }

    public List<Room> getRooms(int level) {

        //components.get()  ///TODO: refactor: access by index not OK, should be based on number, therefore iterate, or store as HashMap.
        List<Room> roomsOnLevel = new ArrayList<>();
        for (Room room : this.rooms) {
            if (room.getLevel() == level) {
                //returnValue = room;
                roomsOnLevel.add(room);
            }
        }

        return roomsOnLevel;
    }

    /**
     * Convenience method.
     * Until a better Jackson ObjectMapper implementation, loop through all rooms and replace component number by actual object reference.
     */
    public void initRooms() {
        for (Room room : this.rooms) {
            Set<Function> functions = room.getComponentTypes().keySet();
            for (Function function : functions) {
                List<Integer> componentTypes = room.getComponentTypes().get(function);
                for (Integer componentNumber : componentTypes) {
                    TDSComponent component = this.getComponent(function, componentNumber);
                    Map<Function, List<TDSComponent>> components = room.getComponents();
                    List<TDSComponent> tdsComponents = components.get(function);
                    if (tdsComponents == null || tdsComponents.size() <= 0) {
                        tdsComponents = new ArrayList<>();
                        components.put(function, tdsComponents);
                    }
                    tdsComponents.add(component);
                }
            }
        }
    }

    public static TDSClientConfig read(InputStream jsonData) throws IOException {
        TDSClientConfig clientConfig = null;
        LOG.debug("##### TDSClient initialization - START");
        //TODO: find better way to load the config
        // Should we load and parse the config to POJO from a URL using JAX-RS?
        // This way the config can be loaded from webroot as a simple resource, or from any location.

        //read json file data to String
//            InputStream jsonData = this.getClass().getClassLoader().getResourceAsStream("tds-config.json");

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //convert json string to object
        clientConfig = objectMapper.readValue(jsonData, TDSClientConfig.class);
        LOG.debug("Config loaded: TDS HOST: " + clientConfig.getHost() + ":" + clientConfig.getPort() + " - TESTMODE: " + clientConfig.isTestMode());

        // until a better Jackson ObjectMapper implementation, loop through all rooms and replace component number by actual object reference
        clientConfig.initRooms();

            /*
            // don't connect to the TDS server if testMode is enabled in the JSON config; his enables us to test the UI.
            if (!clientConfig.isTestMode()) {
                // TODO: define timeout for connection attempt.
                this.createSocket(clientConfig.getHost(), clientConfig.getPort());

                // retrieve all initial component states
                this.getInitialComponentStates();
            }
            */

        LOG.debug("##### TDSClient initialization - END");

        return clientConfig;
    }
}
