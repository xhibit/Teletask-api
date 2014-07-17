package be.xhibit.teletask.parser;

public class InterestingProprietaryModelConsumerImpl extends FullProprietaryModelConsumerImpl {
    @Override
    public void visitInput(String autobusId, String autobusType, String autobusNumber, String id, String name, String shortActionType, String shortActionId, String longActionType, String longActionId) {
//        super.visitInput(autobusId, autobusType, autobusNumber, id, name, shortActionType, shortActionId, longActionType, longActionId);
    }

    @Override
    public void visitRelay(String id, String roomName, String type, String description) {
        super.visitRelay(id, roomName, type, description);
    }

    @Override
    public void visitInputInterface(String autobusId, String autobusType, String autobusNumber, String name) {
//        super.visitInputInterface(autobusId, autobusType, autobusNumber, name);
    }

    @Override
    public void visitOutputInterface(String autobusId, String autobusType, String autobusNumber, String type, String name) {
//        super.visitOutputInterface(autobusId, autobusType, autobusNumber, type, name);
    }

    @Override
    public void visitRoom(String id, String name) {
        super.visitRoom(id, name);
    }

    @Override
    public void visitMacAddress(String value) {
        super.visitMacAddress(value);
    }

    @Override
    public void visitPortNumber(String value) {
        super.visitPortNumber(value);
    }

    @Override
    public void visitIpAddress(String value) {
        super.visitIpAddress(value);
    }

    @Override
    public void visitSerialNumber(String value) {
        super.visitSerialNumber(value);
    }

    @Override
    public void visitType(String value) {
        super.visitType(value);
    }

    @Override
    public void visitName(String value) {
        super.visitName(value);
    }

    @Override
    public void visitPrincipalSite(String value) {
        super.visitPrincipalSite(value);
    }
}