package de.ckthomas.smarthome.services;

import com.google.gson.Gson;
import de.ckthomas.smarthome.exceptions.HassioException;
import org.camunda.bpm.engine.RuntimeService;
import org.eclipse.paho.client.mqttv3.*;
import org.python.antlr.ast.Exec;
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
    protected final String[] mqttTopics;

    protected final RuntimeService runtimeService;
    protected IMqttClient mqttClient = null;
    protected final Gson gson = new Gson();

    protected AbstractMqttService(Class<?> loggerClass, RuntimeService runtimeService, String serverURI,
                                  String username, char[] password, String uniqueClientId,
                                  String... mqttTopics) {
        LOGGER = LoggerFactory.getLogger(loggerClass);
        this.runtimeService = runtimeService;
        this.mqttTopics = mqttTopics;
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
    protected abstract void handleMessage(String topic, MqttMessage message) throws HassioException;

    /**
     * Helper method to check, if given string is a JSON or not.
     *
     * NOTE: If a simple String is given, also false will be returned!
     *
     * @param valToCheck
     * @return true or false
     */
    protected boolean isJson(String valToCheck) {
        try {
            Object json = gson.fromJson(valToCheck, Object.class);
            return !String.class.equals(json.getClass()) ? true : false;
        } catch (Exception e) {
            return false;
        }
    }

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
            mqttClient.setCallback(this);
        } catch (Exception e) {
            throw new HassioException("Could not connect to mqtt broker", e);
        }
    }

    public void subscribe(String... mqttTopics) throws HassioException {
        try {
            LOGGER.info("About to subscribe to topics = '{}'", mqttTopics);
            mqttClient.subscribe(mqttTopics);
        } catch (Exception e) {
            throw new HassioException("Could not subscribe ", e);
        }
    }

    public void subscribe() throws HassioException {
        subscribe(mqttTopics);
    }

    public void unsubscribe(String... mqttTopics) throws HassioException {
        try {
            LOGGER.info("About to unsubscribe following topics = '{}'", mqttTopics);
            mqttClient.unsubscribe(mqttTopics);
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
            mqttClient.unsubscribe(mqttTopics);
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
        try {
            LOGGER.debug("new incoming message @ topic {} with content = {}", topic, message);
            handleMessage(topic, message);
        } catch (Exception e) {
            LOGGER.error("Something went wrong while handling arrived message = " + message, e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        LOGGER.debug("delivery is complete. Token = {}", token);
    }
}
