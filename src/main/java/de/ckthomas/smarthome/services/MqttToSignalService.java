package de.ckthomas.smarthome.services;

import com.google.gson.reflect.TypeToken;
import de.ckthomas.smarthome.exceptions.HassioException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.SignalEventReceivedBuilder;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Christian Thomas
 */
public class MqttToSignalService extends AbstractMqttService {

    MqttToSignalService(RuntimeService runtimeService, String serverURI, String username, char[] password,
                        String uniqueClientId, String... mqttProcessStartTopic) {
        super(MqttToSignalService.class,
                runtimeService,
                serverURI,
                username,
                password,
                uniqueClientId,
                mqttProcessStartTopic);
    }

    @Override
    protected void handleMessage(String topic, MqttMessage message) throws HassioException {
        final String payload = message.toString();
        try {
            LOGGER.info("About to handle message for topic = {} with payload = {}", topic, message);
            final SignalEventReceivedBuilder signalEventBuilder = runtimeService.createSignalEvent(topic);

            if (isJson(payload)) {
                Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
                Map<String, Object> processVariables = gson.fromJson(payload, mapType);
                LOGGER.info("Found JSON Payload = {}. Parse JSON with key + value as process variable map = {}",
                        payload, processVariables);
                signalEventBuilder.setVariables(processVariables);
            }

            signalEventBuilder.send();

        } catch (Exception e) {
            throw new HassioException("Could not handle MqttMessage to transfer it to a bpmn signal! Payload = " +
                    payload, e);
        }
    }

    // TODO WRONG PLACE HERE?!?!?!
    /**
     * Useful to subscribe during process runtime execution a specific topic
     *
     * @param topic
     * @param executionId
     * @param activityInstanceId
     */
    public void addRuntimeSubscription(String topic, String executionId, String activityInstanceId) {

    }

    // TODO WRONG PLACE HERE?!?!?!
    public void removeRuntimeSubscription(String executionId, String activityInstanceId) {

    }
}