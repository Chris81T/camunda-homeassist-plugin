package de.ckthomas.smarthome.services;

import com.google.gson.reflect.TypeToken;
import de.ckthomas.smarthome.exceptions.HassioException;
import kotlin.Pair;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.SignalEventReceivedBuilder;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Christian Thomas
 */
public class MqttToSignalService extends AbstractMqttService {

    /**
     * outer key is the topic
     * List holds a bunch of pair
     * pair key is a processInstanceId:activityInstanceId combination
     * pair value is the resultVariable name
     */
    private static final Map<String, List<Pair<String, Optional<String>>>> tempRuntimeSubscriptions = new HashMap<>();


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

    private SignalEventReceivedBuilder setVariablesToEventBuilder(SignalEventReceivedBuilder builder,
                                                                  Map<String, Object> processVariables) {
        LOGGER.info("Following process values are prepared to send within the new signal. Variables = {}",
                processVariables);
        builder.setVariables(processVariables);
        return builder;
    }

    @Override
    protected void handleMessage(String topic, MqttMessage message) throws HassioException {
        final String payload = message.toString();
        try {
            LOGGER.info("About to handle message for topic = {} with payload = {}", topic, message);
            final SignalEventReceivedBuilder signalEventBuilder = runtimeService.createSignalEvent(topic);

            switch (checkValueType(payload)) {
                case JSON_ENTRY: {
                    LOGGER.info("Found JSON_ENTRY payload = {}", payload);
                    Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
                    Map<String, Object> processVariables = gson.fromJson(payload, mapType);
                    setVariablesToEventBuilder(signalEventBuilder, processVariables);
                    break;
                }
                case PRIMITIVE_ENTRY: {
                    LOGGER.info("Found JSON_ENTRY payload = {}", payload);
                    Object primitiveValue = gson.fromJson(payload, Object.class);

                    resultVariable.ifPresentOrElse(
                            resultVariable -> {
                                Map<String, Object> processVariables = new HashMap<>();
                                processVariables.put(resultVariable, primitiveValue);
                                setVariablesToEventBuilder(signalEventBuilder, processVariables);
                            },
                            () -> LOGGER.warn("No resultValue is set! Ignore the JSON_ENTRY payload!")
                    );

                    break;
                }
                case ARRAY: {
                    LOGGER.info("Found ARRAY payload = {}", payload);
                    Type arrayType = new TypeToken<List<Object>>() {}.getType();
                    List<Object> arrayValue = gson.fromJson(payload, arrayType);

                    resultVariable.ifPresentOrElse(
                            resultVariable -> {
                                Map<String, Object> processVariables = new HashMap<>();
                                processVariables.put(resultVariable, arrayValue);
                                setVariablesToEventBuilder(signalEventBuilder, processVariables);
                            },
                            () -> LOGGER.warn("No resultValue is set! Ignore the ARRAY payload!")
                    );

                    break;
                }
                case UNKNOWN: {
                    LOGGER.warn("Given payload = {} is unknown! Ignore it!", payload);
                }
            }

            signalEventBuilder
                    .executionId("") // executionId – the id of the process instance or the execution to deliver the signal to
                    .send();

        } catch (Exception e) {
            throw new HassioException("Could not handle MqttMessage to transfer it to a bpmn signal! Payload = " +
                    payload, e);
        }
    }

    private String combineInstanceIds(String processInstanceId, String activityInstanceId) {
        return new StringBuilder()
                .append(processInstanceId)
                .append(":")
                .append(activityInstanceId)
                .toString();
    }

    /**
     * Useful to subscribe during process runtime execution a specific topic
     *
     * @param topic
     * @param processInstanceId
     * @param activityInstanceId
     */
    public void addTempRuntimeSubscription(String topic, String processInstanceId, String activityInstanceId,
                                           Optional<String> resultVariable) {

        final String combination = combineInstanceIds(processInstanceId, activityInstanceId);
        if (!tempRuntimeSubscriptions.containsKey(combination)) {
            final MqttToSignalService newInstance = getInstance(runtimeService, topic);
            newInstance.setResultVariableForNonJsonMessages(resultVariable);
            newInstance.start();
            tempRuntimeSubscriptions.put(combination, newInstance);
        } else {
            LOGGER.warn("MqttToSignalService for id = {} already exist! Do nothing!", combination);
        }
    }

    public void removeTempRuntimeSubscription(String topic, String processInstanceId, String activityInstanceId) {
        final String combination = combineInstanceIds(processInstanceId, activityInstanceId);

        if (tempRuntimeSubscriptions.containsKey(combination)) {
            MqttToSignalService runningInstance = tempRuntimeSubscriptions.remove(combination);
            runningInstance.unsubscribe(topic);
            runningInstance.close();
        } else {
            LOGGER.warn("Could not unsubscribe/destruct, while given id = {} is unknown! Do nothing!", combination);
        }
    }

}
