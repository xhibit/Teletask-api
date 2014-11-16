package be.xhibit.teletask.mqtt;

import be.xhibit.teletask.ClientHolder;
import be.xhibit.teletask.client.listener.StateChangeListener;
import be.xhibit.teletask.model.spec.ComponentSpec;
import be.xhibit.teletask.mqtt.client.MqttPublisher;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * http://jpmens.net/2013/09/01/installing-mosquitto-on-a-raspberry-pi/
 */
public class MqttExample {
    /**
     * Logger responsible for logging and debugging statements.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MqttExample.class);

    public static void main(String[] args) {
//        MqttBroker mqttBroker = MqttBroker.INSTANCE;

        MqttPublisher.INSTANCE.subscribe(ClientHolder.getClient().getConfig().getAllComponents());

        ClientHolder.getClient().registerStateChangeListener(new StateChangeListener() {
            @Override
            public void event(List<ComponentSpec> components) {
                for (ComponentSpec component : components) {
                    try {
                        MqttPublisher.INSTANCE.publish(component);
                    } catch (MqttException e) {
                        LOG.error("Exception ({}) caught in event: {}", e.getClass().getName(), e.getMessage(), e);
                    }
                }
            }
        });
    }
}
