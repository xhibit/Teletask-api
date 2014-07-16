package be.xhibit.teletask.parser;

import be.xhibit.teletask.model.proprietary.CentralUnit;
import be.xhibit.teletask.model.proprietary.InputInterface;
import be.xhibit.teletask.model.proprietary.OutputInterface;
import be.xhibit.teletask.model.proprietary.Relay;
import be.xhibit.teletask.model.proprietary.Room;

public class ProprietaryModelConsumerImpl implements Consumer {
    private final CentralUnit centralUnit;

    public ProprietaryModelConsumerImpl() {
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
        this.getCentralUnit().setIpAddress(value);
    }

    @Override
    public void visitPortNumber(String value) {
        this.getCentralUnit().setPortNumber(value);
    }

    @Override
    public void visitMacAddress(String value) {
        this.getCentralUnit().setMacAddress(value);
    }

    @Override
    public void visitRoom(String id, String name) {
        this.getCentralUnit().getRooms().add(new Room(id, name));
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
        this.getCentralUnit().getRelays().add(new Relay(id, room, type, description));
    }

    public CentralUnit getCentralUnit() {
        return this.centralUnit;
    }
}