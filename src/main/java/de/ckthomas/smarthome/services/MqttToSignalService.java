package de.ckthomas.smarthome.services;

import com.google.gson.reflect.TypeToken;
import de.ckthomas.smarthome.exceptions.HassioException;
import kotlin.Pair;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.SignalEventReceivedBuilder;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Christian Thomas
 */
public class MqttToSignalService extends AbstractMqttService {

    /**
     * outer key is the topic
     * List holds a bunch of pair
     * pair key is the processInstanceId
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

    /**
     * Useful to subscribe during process runtime execution a specific topic
     *
     * @param topic
     * @param processInstanceId
     * @param resultVariable NOTE: If in the same process two signals listening to the same topic, make sure, that the
     *                       same resultVariable is defined! It is impossible to differ between signals in the same
     *                       process instance
     */
    public void addTempRuntimeSubscription(String topic, String processInstanceId, Optional<String> resultVariable) {

        boolean mqttSubscriptionNeeded = false;

        if (!tempRuntimeSubscriptions.containsKey(topic)) {
            LOGGER.info("Record new topic = {} with empty list...", topic);
            tempRuntimeSubscriptions.put(topic, new ArrayList<>());
            mqttSubscriptionNeeded = true;
        }

        List<Pair<String, Optional<String>>> list = tempRuntimeSubscriptions.get(topic);
        list.stream()
            .filter(pair -> pair.component1().equals(processInstanceId))
            .findFirst()
            .ifPresentOrElse(
                foundPair -> {
                    LOGGER.warn("For topic = {} and process instance id = {} an entry already exist! Ignore new one " +
                            "with resultVariable = {} / foundPair = {}",
                            topic, processInstanceId, resultVariable, foundPair);
                }, () -> {
                    LOGGER.info("Register new entry for topic = {}, process instance id = {} with resultVariable = {}",
                            topic, processInstanceId, resultVariable);
                    list.add(new Pair<>(processInstanceId, resultVariable));
                }
            );

        if (mqttSubscriptionNeeded) {
            LOGGER.info("After registering new topic = {} with first process instance id = {} also subscribe topic at " +
                    "mqtt broker.", topic, processInstanceId);
            subscribe(topic);
        }
    }

    public void removeTempRuntimeSubscription(String topic, String processInstanceId) {
        if (tempRuntimeSubscriptions.containsKey(topic)) {
            List<Pair<String, Optional<String>>> list = tempRuntimeSubscriptions.get(topic);
            LOGGER.info("Check list = {} to remove topic = {} in combination with process instance id = {}",
                    list, topic, processInstanceId);

            List<Pair<String, Optional<String>>> filteredList = list.stream()
                    .filter(pair -> !pair.component1().equals(processInstanceId))
                    .collect(Collectors.toList());

            if (filteredList.size() != list.size()) {
                tempRuntimeSubscriptions.put(topic, filteredList);
            } else {
                LOGGER.warn("No entry found for process instance id = {} in combination with topic = {}. Do nothing!",
                        processInstanceId, topic);
            }

            if (filteredList.isEmpty()) {
                LOGGER.info("After removing process instance id = {} from topic = {}, no further entry available. " +
                        "Remove topic entry and unsubscribe it from mqtt broker.", processInstanceId, topic);
                tempRuntimeSubscriptions.remove(topic);
                unsubscribe(topic);
            }
        } else {
            LOGGER.warn("No entry for topic = {} found! Nothing to remove. Ignore request!", topic);
        }
    }

}
