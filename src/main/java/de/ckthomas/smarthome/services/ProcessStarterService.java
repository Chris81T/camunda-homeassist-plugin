package de.ckthomas.smarthome.services;

import com.google.gson.Gson;
import de.ckthomas.smarthome.dtos.ProcessStartDto;
import de.ckthomas.smarthome.exceptions.HassioException;
import org.camunda.bpm.engine.RuntimeService;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author Christian Thomas
 */
public class    ProcessStarterService implements MqttCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessStarterService.class);

    private final String serverURI;
    private final String username;
    private final char[] password;

    private final String uniqueClientId;
    private final String mqttProcessStartTopic;

    private final RuntimeService runtimeService;
    private IMqttClient mqttClient = null;
    private final Gson gson = new Gson();

    ProcessStarterService(RuntimeService runtimeService, String serverURI, String username, char[] password,
                          String mqttProcessStartTopic, String uniqueClientId) {
        this.runtimeService = runtimeService;
        this.mqttProcessStartTopic = mqttProcessStartTopic;
        this.serverURI = serverURI;
        this.username = username;
        this.password = password;
        this.uniqueClientId = uniqueClientId;
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

        } catch (Exception e) {
            throw new HassioException("Could not connect to mqtt broker", e);
        }
    }

    public void subscribe() throws HassioException {
        try {
            mqttClient.setCallback(this);
            LOGGER.info("About to subscribe to '{}'", mqttProcessStartTopic);
            mqttClient.subscribe(mqttProcessStartTopic);
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
            mqttClient.unsubscribe(mqttProcessStartTopic);
            mqttClient.disconnect();
            mqttClient.close();
        } catch (Exception e) {
            throw new HassioException("Could not unsubscribe or close the mqtt connection", e);
        }
    }

    /**
     * Falls es ausgelagert wird, kann dies hier eine abstrakte Methode werden und dann spezifisch umgesetzt werden
     *
     * @param message
     */
    protected void handleMessage(MqttMessage message) throws HassioException {
        final String payload = message.toString();
        try {
            LOGGER.info("Incoming payload = {} to start new process instance", payload);
            final ProcessStartDto dto = gson.fromJson(payload, ProcessStartDto.class);
            LOGGER.info("Parsed dto from payload = {}", dto);
            if (dto.getBusinessKey() != null) {
                runtimeService.startProcessInstanceByKey(dto.getProcessDefinitionKey(), dto.getBusinessKey(),
                        dto.getVariables());
            } else {
                runtimeService.startProcessInstanceByKey(dto.getProcessDefinitionKey(), dto.getVariables());
            }
        } catch (Exception e) {
            throw new HassioException("Could not start process with key = " + payload, e);
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
