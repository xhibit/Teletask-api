package be.xhibit.teletask.client.builder.composer.config.sensor;

import be.xhibit.teletask.client.builder.composer.config.NumberConverter;
import be.xhibit.teletask.model.spec.ComponentSpec;

import java.util.HashMap;
import java.util.Map;

public class MappingStateCalculator extends SimpleStateCalculator {
    private final Map<String, String> byName = new HashMap<>();
    private final Map<Number, String> byNumber = new HashMap<>();

    public MappingStateCalculator(NumberConverter numberConverter, StateMapping... mappings) {
        super(numberConverter);
        this.register(mappings);
    }

    private void register(StateMapping... mappings) {
        for (StateMapping mapping : mappings) {
            this.byName.put(mapping.getName().toUpperCase(), String.valueOf(mapping.getNumber()));
            this.byNumber.put(mapping.getNumber(), mapping.getName().toUpperCase());
        }
    }

    @Override
    public String convertGet(ComponentSpec component, byte[] value) {
        return this.byNumber.get(this.getNumberConverter().convert(value));
    }

    @Override
    public byte[] convertSet(ComponentSpec component, String value) {
        return super.convertSet(component, String.valueOf(this.byName.get(value)));
    }

    @Override
    public boolean isValidState(String state) {
        return this.byName.keySet().contains(state.toUpperCase());
    }
}
