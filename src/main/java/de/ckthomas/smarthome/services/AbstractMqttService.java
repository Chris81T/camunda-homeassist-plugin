package de.ckthomas.smarthome.services;

import com.google.gson.Gson;
import de.ckthomas.smarthome.exceptions.HassioException;
import org.camunda.bpm.engine.RuntimeService;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Christian Thomas
 */
public abstract class AbstractMqttService implements MqttCallback {

    protected final Logger LOGGER;

    protected final String serverURI;
    protected final String username;
    protected final char[] password;

    protected final String uniqueClientId;
    protected final String mqttTopic;

    protected final RuntimeService runtimeService;
    protected IMqttClient mqttClient = null;
    protected final Gson gson = new Gson();

    protected AbstractMqttService(Class<?> loggerClass, RuntimeService runtimeService, String serverURI,
                                  String username, char[] password, String mqttTopic,
                                  String uniqueClientId) {
        LOGGER = LoggerFactory.getLogger(loggerClass);
        this.runtimeService = runtimeService;
        this.mqttTopic = mqttTopic;
        this.serverURI = serverURI;
        this.username = username;
        this.password = password;
        this.uniqueClientId = uniqueClientId;
    }

    /**
     * must be provided from each implementation
     *
     * @param message
     * @throws HassioException
     */
    protected abstract void handleMessage(MqttMessage message) throws HassioException;

    public void connect() throws HassioException {
        try {
            LOGGER.info("About to connect to Mqtt Broker = '{}'", serverURI);
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
            throw new HassioException("Could not connect to mqtt broker", e);
        }
    }

    public void subscribe() throws HassioException {
        try {
            mqttClient.setCallback(this);
            LOGGER.info("About to subscribe to '{}'", mqttTopic);
            mqttClient.subscribe(mqttTopic);
        } catch (Exception e) {
            throw new HassioException("Could not subscribe ", e);
        }
    }

    public void start() throws HassioException {
        connect();
        subscribe();
    }

    public void close() throws HassioException {
        try {
            mqttClient.unsubscribe(mqttTopic);
            mqttClient.disconnect();
            mqttClient.close();
        } catch (Exception e) {
            throw new HassioException("Could not unsubscribe or close the mqtt connection", e);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        LOGGER.warn("Connection lost!", cause);
        try {
            mqttClient.reconnect();
        } catch (MqttException e) {
            LOGGER.error("Could not reconnect to MQTT broker! Check exception = ", e);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        LOGGER.debug("new incoming message @ topic {} with content = {}", topic, message);
        handleMessage(message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        LOGGER.debug("delivery is complete. Token = {}", token);
    }
}
