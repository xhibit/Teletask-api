package be.xhibit.teletask.client.listener;

import be.xhibit.teletask.model.spec.ComponentSpec;

import java.util.List;

public interface StateChangeListener {
    void event(List<ComponentSpec> components);
}
