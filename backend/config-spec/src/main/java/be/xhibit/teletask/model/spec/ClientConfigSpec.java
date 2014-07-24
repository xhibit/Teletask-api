package be.xhibit.teletask.model.spec;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize(as=ClientConfigSpec.class)
public interface ClientConfigSpec {
    String getHost();

    int getPort();

    ComponentSpec getComponent(Function function, int number);

    CentralUnitType getCentralUnitType();

    List<? extends RoomSpec> getRooms();
}
