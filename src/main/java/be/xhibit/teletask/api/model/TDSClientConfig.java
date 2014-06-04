package be.xhibit.teletask.api.model;

import be.xhibit.teletask.api.enums.Function;

import java.util.List;
import java.util.Map;

/**
 * POJO representation of the TDS config JSON file.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class TDSClientConfig {
    private String host;
    private int port;
    private Map<Function, List<TDSComponent>> componentsTypes;
    private List<Room> rooms;

    /**
     * Default constructor.
     */
    public TDSClientConfig() {
    }

    public TDSClientConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Map<Function, List<TDSComponent>> getComponentsTypes() {
        return componentsTypes;
    }

    public void setComponentsTypes(Map<Function, List<TDSComponent>> componentsTypes) {
        this.componentsTypes = componentsTypes;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    // ================================ HELPER METHODS

    public TDSComponent getComponent(Function function, int number) {
        TDSComponent returnValue = null;

        List<TDSComponent> components = this.componentsTypes.get(function);

        //components.get()  ///TODO: refactor: access by index not OK, should be based on number, therefore iterate, or store as HashMap.
        for (TDSComponent component: components) {
            if (component.getNumber() == number) {
                component.setFunction(function);
                returnValue = component;
                break;
            }
        }

        return returnValue;
    }

    public Room getRoom(int level) {
        Room returnValue = null;

        //components.get()  ///TODO: refactor: access by index not OK, should be based on number, therefore iterate, or store as HashMap.
        for (Room room: rooms) {
            if (room.getLevel() == level) {
                returnValue = room;
                //room.setComponents();
                break;
            }
        }

        return returnValue;
    }
}
