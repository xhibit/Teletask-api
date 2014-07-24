package be.xhibit.teletask.parser;

import be.xhibit.teletask.model.nbt.CentralUnit;
import be.xhibit.teletask.model.nbt.Input;
import be.xhibit.teletask.model.nbt.InputInterface;
import be.xhibit.teletask.model.nbt.OutputInterface;
import be.xhibit.teletask.model.nbt.Relay;
import be.xhibit.teletask.model.nbt.Room;
import be.xhibit.teletask.model.spec.ComponentSpec;
import be.xhibit.teletask.model.spec.Function;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FullNbtModelConsumerImpl implements Consumer {
    /**
     * Logger responsible for logging and debugging statements.
     */
    private static final Logger LOG = LoggerFactory.getLogger(FullNbtModelConsumerImpl.class);

    private final CentralUnit centralUnit;

    public FullNbtModelConsumerImpl() {
        this.centralUnit = new CentralUnit();
    }

    @Override
    public void principalSite(String value) {
        this.getLogger().debug("principalSite: {}", value);
        this.getCentralUnit().setPrincipalSite(value);
    }

    @Override
    public void name(String value) {
        this.getLogger().debug("name: {}", value);
        this.getCentralUnit().setName(value);
    }

    @Override
    public void type(String value) {
        this.getLogger().debug("type: {}", value);
        this.getCentralUnit().setType(value);
    }

    @Override
    public void serialNumber(String value) {
        this.getLogger().debug("serialNumber: {}", value);
        this.getCentralUnit().setSerialNumber(value);
    }

    @Override
    public void ipAddress(String value) {
        this.getLogger().debug("ipAddress: {}", value);
        this.getCentralUnit().setHost(value);
    }

    @Override
    public void portNumber(String value) {
        this.getLogger().debug("portNumber: {}", value);
        this.getCentralUnit().setPort(Integer.valueOf(value));
    }

    @Override
    public void macAddress(String value) {
        this.getLogger().debug("macAddress: {}", value);
        this.getCentralUnit().setMacAddress(value);
    }

    @Override
    public void room(String id, String name) {
        this.getLogger().debug("room: {} - {}", id, name);
        this.getCentralUnit().getRooms().add(new Room(Integer.valueOf(id), name));
    }

    @Override
    public void outputInterface(String autobusId, String autobusType, String autobusNumber, String type, String name) {
        this.getLogger().debug("outputInterface: {}:{}:{} {} - {}", autobusId, autobusType, autobusNumber, type, name);
        this.getCentralUnit().getOutputInterfaces().add(new OutputInterface(autobusId, autobusType, autobusNumber, type, name));
    }

    @Override
    public void inputInterface(String autobusId, String autobusType, String autobusNumber, String name) {
        this.getLogger().debug("inputInterface: {}:{}:{} - {}", autobusId, autobusType, autobusNumber, name);
        this.getCentralUnit().getInputInterfaces().add(new InputInterface(autobusId, autobusType, autobusNumber, name));
    }

    @Override
    public void relay(String id, String roomName, String type, String description) {
        this.getLogger().debug("relay: {}:{} (Room {}) - {}", type, id, roomName, description);
        Room room = this.getCentralUnit().findRoom(roomName);
        Relay relay = new Relay(Integer.valueOf(id), room, type, description);
        room.getRelays().add(relay);
        this.getCentralUnit().getComponents().add(relay);
    }

    @Override
    public void input(String autobusId, String autobusType, String autobusNumber, String id, String name, String shortActionType, String shortActionId, String longActionType, String longActionId) {
        this.getLogger().debug("input: {}:{}:{} - {} - {} [Short: {}:{}], [Long: {}:{}]", autobusId, autobusType, autobusNumber, id, name, shortActionType, shortActionId, longActionType, longActionId);
        InputInterface inputInterface = this.getCentralUnit().findInputInterface(autobusId, autobusType, autobusNumber);

        inputInterface.getInputs().add(new Input(id, name, this.getComponent(shortActionType, shortActionId), this.getComponent(longActionType, longActionId)));
    }

    private ComponentSpec getComponent(String actionType, String actionId) {
        ComponentSpec component = null;
        if (!Strings.isNullOrEmpty(actionType) && !Strings.isNullOrEmpty(actionId)) {
            component = this.getCentralUnit().getComponent(ACTION_MAPPING.get(actionType), Integer.valueOf(actionId));
        }
        return component;
    }

    private static Map<String, Function> ACTION_MAPPING = ImmutableMap.<String, Function>builder()
            .put("REL", Function.RELAY)
            .build();

    public CentralUnit getCentralUnit() {
        return this.centralUnit;
    }

    protected Logger getLogger() {
        return LOG;
    }
}