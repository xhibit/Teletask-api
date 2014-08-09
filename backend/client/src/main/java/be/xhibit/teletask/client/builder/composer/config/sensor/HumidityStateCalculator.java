package be.xhibit.teletask.client.builder.composer.config.sensor;

import be.xhibit.teletask.client.builder.composer.config.NumberConverter;

public class HumidityStateCalculator extends PercentageStateCalculator {
    public HumidityStateCalculator(NumberConverter numberConverter) {
        super(numberConverter);
    }
}