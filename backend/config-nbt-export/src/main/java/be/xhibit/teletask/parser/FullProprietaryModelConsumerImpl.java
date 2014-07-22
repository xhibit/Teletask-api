package be.xhibit.teletask.parser;

import be.xhibit.teletask.model.nbt.CentralUnit;
import be.xhibit.teletask.model.nbt.Input;
import be.xhibit.teletask.model.nbt.InputInterface;
import be.xhibit.teletask.model.nbt.OutputInterface;
import be.xhibit.teletask.model.nbt.Relay;
import be.xhibit.teletask.model.nbt.Room;
import be.xhibit.teletask.model.spec.Component;
import be.xhibit.teletask.model.spec.Function;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class FullProprietaryModelConsumerImpl implements Consumer {
    private final CentralUnit centralUnit;

    public FullProprietaryModelConsumerImpl() {
        this.centralUnit = new CentralUnit();
    }

    @Override
    public void visitPrincipalSite(String value) {
        this.getCentralUnit().setPrincipalSite(value);
    }

    @Override
    public void visitName(String value) {
        this.getCentralUnit().setName(value);
    }

    @Override
    public void visitType(String value) {
        this.getCentralUnit().setType(value);
    }

    @Override
    public void visitSerialNumber(String value) {
        this.getCentralUnit().setSerialNumber(value);
    }

    @Override
    public void visitIpAddress(String value) {
        this.getCentralUnit().setHost(value);
    }

    @Override
    public void visitPortNumber(String value) {
        this.getCentralUnit().setPort(Integer.valueOf(value));
    }

    @Override
    public void visitMacAddress(String value) {
        this.getCentralUnit().setMacAddress(value);
    }

    @Override
    public void visitRoom(String id, String name) {
        this.getCentralUnit().getRooms().add(new Room(Integer.valueOf(id), name));
    }

    @Override
    public void visitOutputInterface(String autobusId, String autobusType, String autobusNumber, String type, String name) {
        this.getCentralUnit().getOutputInterfaces().add(new OutputInterface(autobusId, autobusType, autobusNumber, type, name));
    }

    @Override
    public void visitInputInterface(String autobusId, String autobusType, String autobusNumber, String name) {
        this.getCentralUnit().getInputInterfaces().add(new InputInterface(autobusId, autobusType, autobusNumber, name));
    }

    @Override
    public void visitRelay(String id, String roomName, String type, String description) {
        Room room = this.getCentralUnit().findRoom(roomName);
        Relay relay = new Relay(Integer.valueOf(id), room, type, description);
        room.getRelays().add(relay);
        this.getCentralUnit().getComponents().add(relay);
    }

    @Override
    public void visitInput(String autobusId, String autobusType, String autobusNumber, String id, String name, String shortActionType, String shortActionId, String longActionType, String longActionId) {
        InputInterface inputInterface = this.getCentralUnit().findInputInterface(autobusId, autobusType, autobusNumber);

        inputInterface.getInputs().add(new Input(id, name, this.getComponent(shortActionType, shortActionId), this.getComponent(longActionType, longActionId)));
    }
    private Component getComponent(String actionType, String actionId) {
        return this.getCentralUnit().getComponent(ACTION_MAPPING.get(actionType), Integer.valueOf(actionId));
    }

    private static Map<String, Function> ACTION_MAPPING = ImmutableMap.<String, Function>builder()
            .put("REL", Function.RELAY)
            .build();

    public CentralUnit getCentralUnit() {
        return this.centralUnit;
    }
}