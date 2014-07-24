package be.xhibit.teletask.model.spec;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize(as=RoomSpec.class)
public interface RoomSpec {
    int getId();

    String getName();

    List<? extends ComponentSpec> getRelays();
}