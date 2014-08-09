package be.xhibit.teletask.client.builder.composer.config.sensor;

import be.xhibit.teletask.client.builder.composer.config.NumberConverter;

public class DimmerStateCalculator extends PercentageStateCalculator {
    public DimmerStateCalculator(NumberConverter numberConverter) {
        super(numberConverter);
    }
}