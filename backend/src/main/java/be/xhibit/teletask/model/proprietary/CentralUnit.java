package be.xhibit.teletask.model.proprietary;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class CentralUnit {
    private String principalSite;
    private String name;
    private String type;
    private String serialNumber;
    private String ipAddress;
    private String portNumber;
    private String macAddress;

    private List<Room> rooms;
    private List<OutputInterface> outputInterfaces;
    private List<InputInterface> inputInterfaces;
    private List<Relay> relays;

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

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPortNumber() {
        return this.portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
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

    public List<Relay> getRelays() {
        if (this.relays == null) {
            this.setRelays(new ArrayList<Relay>());
        }
        return this.relays;
    }

    private void setRelays(List<Relay> relays) {
        this.relays = relays;
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

    public Relay findRelay(String id) {
        return this.getRelayMap().get(id);
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

    private <T extends InterfaceSupport> Map<String, T> convertInterfacesToMap(List<T> interfaces) {
        return Maps.uniqueIndex(interfaces, new Function<T, String>() {
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

    private Map<String, Room> convertRoomsToMap(List<Room> rooms) {
        return Maps.uniqueIndex(rooms, new Function<Room, String>() {
            @Override
            public String apply(Room from) {
                return from.getName();
            }
        });
    }
    private transient Map<String, Relay> relayMap;

    private Map<String, Relay> getRelayMap() {
        if (this.relayMap == null) {
            this.relayMap = this.convertRelaysToMap(this.getRelays());
        }
        return this.relayMap;
    }

    private Map<String, Relay> convertRelaysToMap(List<Relay> relays) {
        return Maps.uniqueIndex(relays, new Function<Relay, String>() {
            @Override
            public String apply(Relay from) {
                return from.getId();
            }
        });
    }
}
