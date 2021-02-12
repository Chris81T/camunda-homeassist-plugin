package de.ckthomas.smarthome.services;

import de.ckthomas.smarthome.exceptions.HassioException;
import org.camunda.bpm.engine.RuntimeService;
import org.eclipse.paho.client.mqttv3.*;

/**
 * @author Christian Thomas
 */
public class ProcessStarterService implements MqttCallback {

    private final String serverURI;
    private final String username;
    private final char[] password;

    private final String uniqueClientId;
    private final String mqttProcessStartTopic;

    private final RuntimeService runtimeService;
    private MqttClient mqttClient = null;

    ProcessStarterService(RuntimeService runtimeService, String serverURI, String username, char[] password,
                          String mqttProcessStartTopic, String uniqueClientId) {
        this.runtimeService = runtimeService;
        this.mqttProcessStartTopic = mqttProcessStartTopic;
        this.serverURI = serverURI;
        this.username = username;
        this.password = password;
        this.uniqueClientId = uniqueClientId;
    }

    public void start() throws HassioException {
        try {
            mqttClient = new MqttClient(serverURI, uniqueClientId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            if (username != null) {
                options.setUserName(username);
            }

            if (password != null) {
                options.setPassword(password);
            }

            mqttClient.connect(options);

        } catch (Exception e) {
            throw new HassioException("Could not start the work to listen to mqtt broker, while starting some " +
                    "processes when requested", e);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
