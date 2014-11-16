package be.xhibit.teletask.mqtt.client;

import be.xhibit.teletask.model.spec.ComponentSpec;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public enum MqttPublisher {
    INSTANCE;

    private final Logger log = LoggerFactory.getLogger(MqttPublisher.class);

    private MqttClient client;

    MqttPublisher() {
        this.getClient();
    }

    private IMqttClient getClient() {
        if (this.client == null) {
            String broker = "tcp://raspberry-pi:1883";
            String clientId = "TeletaskClient";
            MemoryPersistence persistence = new MemoryPersistence();
            try {
                this.client = new MqttClient(broker, clientId, persistence);
                this.connect(this.client);
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        try {
                            MqttPublisher.this.client.disconnect();
                        } catch (MqttException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            } catch (MqttException me) {
                log.error("Exception ({}) caught in getClient: {}", me.getClass().getName(), me.getMessage(), me);
            }
        } else if (!this.client.isConnected()) {
            try {
                this.client.connect(this.getConnectOptions());
            } catch (MqttException e) {
                log.error("Exception ({}) caught in getClient: {}", e.getClass().getName(), e.getMessage(), e);
            }
        }

        return this.client;
    }

    private void connect(IMqttClient mqttClient) {
        MqttConnectOptions connOpts = this.getConnectOptions();
        System.out.println("Connecting to broker");
        try {
            mqttClient.connect(connOpts);
        } catch (MqttException e) {
            log.error("Exception ({}) caught in connect: {}", e.getClass().getName(), e.getMessage(), e);
        }
        System.out.println("Connected");
    }

    private MqttConnectOptions getConnectOptions() {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        return connOpts;
    }

    public void subscribe(Iterable<? extends ComponentSpec> allComponents) {
        try {
            for (ComponentSpec componentSpec : allComponents) {
                String topic = this.toTopic(componentSpec);
                System.out.println(String.format("Subscribing to topic '%s'", topic));
                this.getClient().subscribe(topic);
                this.getClient().setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable throwable) {

                    }

                    @Override
                    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                        System.out.println("mqttMessage = " + mqttMessage);
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                    }
                });
            }
        } catch (MqttException e) {
            log.error("Exception ({}) caught in subscribe: {}", e.getClass().getName(), e.getMessage(), e);
        }
    }

    public void publish(ComponentSpec component) throws MqttException {
        MqttMessage message = new MqttMessage(component.getState().getBytes());
        message.setQos(2);
        String topic = this.toTopic(component);
        this.getClient().publish(topic, message);
        System.out.println(String.format("Message '%s' published to topic '%s'", message.toString(), topic));
    }

    private String toTopic(ComponentSpec component) {
        return String.format("/teletask-api/%s/%s", component.getFunction(), component.getNumber());
    }
}
