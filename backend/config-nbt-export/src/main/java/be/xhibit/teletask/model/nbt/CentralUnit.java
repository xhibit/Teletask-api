package be.xhibit.teletask.model.nbt;

import be.xhibit.teletask.model.spec.CentralUnitType;
import be.xhibit.teletask.model.spec.ClientConfig;
import be.xhibit.teletask.model.spec.Component;
import be.xhibit.teletask.model.spec.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CentralUnit implements ClientConfig {
    private static final Map<String, CentralUnitType> CENTRAL_UNIT_TYPE_MAP = ImmutableMap.<String, CentralUnitType>builder()
            .put("TDS 10010: MICROS", CentralUnitType.MICROS)
            .put("TDS 10012: MICROS+", CentralUnitType.MICROS_PLUS)
            .build();

    private String principalSite;
    private String name;
    private String type;
    private String serialNumber;
    private String host;
    private int port;
    private String macAddress;

    private List<Room> rooms;

    private List<OutputInterface> outputInterfaces;
    private List<InputInterface> inputInterfaces;

    private List<ComponentSupport> components;

    public String getPrincipalSite() {
        return this.principalSite;
    }

    public void setPrincipalSite(String principalSite) {
        this.principalSite = principalSite;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public CentralUnitType getCentralUnitType() {
        return CENTRAL_UNIT_TYPE_MAP.get(this.getType());
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

    public String getMacAddress() {
        return this.macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public List<Room> getRooms() {
        if (this.rooms == null) {
            this.setRooms(new ArrayList<Room>());
        }
        return this.rooms;
    }

    private void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<ComponentSupport> getComponents() {
        if (this.components == null) {
            this.setComponents(new ArrayList<ComponentSupport>());
        }
        return this.components;
    }

    private void setComponents(List<ComponentSupport> components) {
        this.components = components;
    }

    public List<OutputInterface> getOutputInterfaces() {
        if (this.outputInterfaces == null) {
            this.setOutputInterfaces(new ArrayList<OutputInterface>());
        }
        return this.outputInterfaces;
    }

    private void setOutputInterfaces(List<OutputInterface> outputInterfaces) {
        this.outputInterfaces = outputInterfaces;
    }

    public List<InputInterface> getInputInterfaces() {
        if (this.inputInterfaces == null) {
            this.setInputInterfaces(new ArrayList<InputInterface>());
        }
        return this.inputInterfaces;
    }

    private void setInputInterfaces(List<InputInterface> inputInterfaces) {
        this.inputInterfaces = inputInterfaces;
    }


    public InputInterface findInputInterface(String autobusId, String autobusType, String autobusNumber) {
        return this.getInputInterfaceMap().get(InterfaceSupport.getCompleteAutobusId(autobusId, autobusType, autobusNumber));
    }

    public OutputInterface findOutputInterface(String autobusId, String autobusType, String autobusNumber) {
        return this.getOutputInterfaceMap().get(InterfaceSupport.getCompleteAutobusId(autobusId, autobusType, autobusNumber));
    }

    public Room findRoom(String name) {
        return this.getRoomMap().get(name);
    }

    @Override
    public Component getComponent(Function function, int number) {
        return this.getComponentMap().get(this.getIndex(function, number));
    }

    private transient Map<String, InputInterface> inputInterfaceMap;

    private Map<String, InputInterface> getInputInterfaceMap() {
        if (this.inputInterfaceMap == null) {
            this.inputInterfaceMap = this.convertInterfacesToMap(this.getInputInterfaces());
        }
        return this.inputInterfaceMap;
    }

    private transient Map<String, OutputInterface> outputInterfaceMap;

    private Map<String, OutputInterface> getOutputInterfaceMap() {
        if (this.outputInterfaceMap == null) {
            this.outputInterfaceMap = this.convertInterfacesToMap(this.getOutputInterfaces());
        }
        return this.outputInterfaceMap;
    }

    private <T extends InterfaceSupport> Map<String, T> convertInterfacesToMap(Iterable<T> interfaces) {
        return Maps.uniqueIndex(interfaces, new com.google.common.base.Function<T, String>() {
            @Override
            public String apply(T from) {
                return from.getCompleteAutobusId();
            }
        });
    }

    private transient Map<String, Room> roomMap;

    private Map<String, Room> getRoomMap() {
        if (this.roomMap == null) {
            this.roomMap = this.convertRoomsToMap(this.getRooms());
        }
        return this.roomMap;
    }

    private Map<String, Room> convertRoomsToMap(Iterable<Room> rooms) {
        return Maps.uniqueIndex(rooms, new com.google.common.base.Function<Room, String>() {
            @Override
            public String apply(Room from) {
                return from.getName();
            }
        });
    }
    private transient Map<String, ComponentSupport> componentMap;

    private Map<String, ComponentSupport> getComponentMap() {
        if (this.componentMap == null) {
            this.componentMap = this.convertComponentsToMap(this.getComponents());
        }
        return this.componentMap;
    }

    private Map<String, ComponentSupport> convertComponentsToMap(Iterable<ComponentSupport> components) {
        return Maps.uniqueIndex(components, new com.google.common.base.Function<ComponentSupport, String>() {
            @Override
            public String apply(ComponentSupport from) {
                return CentralUnit.this.getIndex(from.getComponentFunction(), from.getId());
            }
        });
    }

    private String getIndex(Function function, int id) {
        return function + ":" + id;
    }
}
