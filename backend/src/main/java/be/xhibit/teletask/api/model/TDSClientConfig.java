package be.xhibit.teletask.api.model;

import be.xhibit.teletask.api.enums.Function;

import java.util.*;

/**
 * POJO representation of the TDS config JSON file.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class TDSClientConfig {
    private String host;
    private int port;
    private boolean testMode;
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

    public boolean isTestMode() {
        return testMode;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
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

    public List<Room> getRooms(int level) {

        //components.get()  ///TODO: refactor: access by index not OK, should be based on number, therefore iterate, or store as HashMap.
        List<Room> roomsOnLevel = new ArrayList<Room>();
        for (Room room: rooms) {
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
        for (Room room: rooms) {
            Set<Function> functions = room.getComponentTypes().keySet();
            for (Function function : functions) {
                List<Integer> componentTypes = room.getComponentTypes().get(function);
                for (Integer componentNumber : componentTypes) {
                    TDSComponent component = this.getComponent(function, componentNumber);
                    HashMap<Function, List<TDSComponent>> components = room.getComponents();
                    List<TDSComponent> tdsComponents = components.get(function);
                    if (tdsComponents == null || tdsComponents.size() <= 0) {
                        tdsComponents = new ArrayList<TDSComponent>();
                        components.put(function, tdsComponents);
                    }
                    tdsComponents.add(component);
                }
            }
        }
    }
}
