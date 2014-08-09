package be.xhibit.teletask.client.builder.composer.config.sensor;

import be.xhibit.teletask.client.builder.composer.config.NumberConverter;
import be.xhibit.teletask.model.spec.ComponentSpec;

public class SimpleStateCalculator implements StateCalculator {
    private final NumberConverter numberConverter;

    public SimpleStateCalculator(NumberConverter numberConverter) {
        this.numberConverter = numberConverter;
    }

    @Override
    public String convertGet(ComponentSpec component, byte[] value) {
        Number number = this.numberConverter.convert(value);
        return String.valueOf(number == -1 ? 255 : number);
    }

    @Override
    public byte[] convertSet(ComponentSpec component, String value) {
        return this.numberConverter.convert(value);
    }

    @Override
    public NumberConverter getNumberConverter() {
        return this.numberConverter;
    }

    @Override
    public boolean isValidState(String state) {
        return true;
    }

    @Override
    public String getDefaultState() {
        return "0";
    }
}
