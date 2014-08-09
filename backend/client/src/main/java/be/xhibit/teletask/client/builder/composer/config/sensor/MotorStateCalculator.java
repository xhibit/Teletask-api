package be.xhibit.teletask.client.builder.composer.config.sensor;

import be.xhibit.teletask.client.builder.composer.config.NumberConverter;

public class MotorStateCalculator extends MappingStateCalculator {
    public MotorStateCalculator(NumberConverter numberConverter, Number up, Number down, Number stop) {
        super(numberConverter, new StateMapping("UP", up), new StateMapping("DOWN", down), new StateMapping("STOP", stop));
    }
}
