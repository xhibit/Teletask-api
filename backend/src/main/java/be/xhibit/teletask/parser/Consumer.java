package be.xhibit.teletask.parser;

public interface Consumer {
    void visitPrincipalSite(String value);

    void visitName(String value);

    void visitType(String value);

    void visitSerialNumber(String value);

    void visitIpAddress(String value);

    void visitPortNumber(String value);

    void visitMacAddress(String value);

    void visitRoom(String id, String name);

    void visitOutputInterface(String autobusId, String autobusType, String autobusNumber, String type, String name);

    void visitInputInterface(String autobusId, String autobusType, String autobusNumber, String name);

    void visitRelay(String id, String roomName, String type, String description);
}