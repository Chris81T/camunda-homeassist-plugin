package de.ckthomas.smarthome.services;

import com.google.gson.reflect.TypeToken;
import de.ckthomas.smarthome.camunda.PluginConsts;
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

    private Optional<String> determineVariableName(boolean processInstanceExists, Optional<String> resultVariable,
                                         Optional<String> fallbackVariable) {

        String determinedName = resultVariable.orElseGet(() -> {
            LOGGER.info("No resultValue is set! Check if a process instance id + fallbackVariable is set");
            if (processInstanceExists) {
                return fallbackVariable.orElseGet(() -> {
                    LOGGER.warn("Neither a resultVariable nor a fallbackVariable is given. So it" +
                                    "is impossible to determine a value as a process variable!");
                    return null;
                });
            }
            return PluginConsts.EngineListener.SIGNAL_START_RESULT_VAR_NAME;
        });

        return Optional.ofNullable(determinedName);
    }

    private Map<String, Object> prepareProcessVariables(String payload, ValueTypes valueType, boolean processInstanceExists,
                                                        Optional<String> resultVariable, Optional<String> fallbackVariable) {
        switch (valueType) {
            case JSON_ENTRY: {
                LOGGER.info("Found JSON_ENTRY payload = {}", payload);
                Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
                Map<String, Object> processVariables = gson.fromJson(payload, mapType);
                return processVariables;
            }
            case PRIMITIVE_ENTRY: {
                LOGGER.info("Found PRIMITIVE_ENTRY payload = {}", payload);
                Object primitiveValue = gson.fromJson(payload, Object.class);

                Map<String, Object> processVariables = new HashMap<>();

                determineVariableName(
                        processInstanceExists,
                        resultVariable,
                        fallbackVariable
                ).ifPresent(variableName -> processVariables.put(variableName, primitiveValue));

                return processVariables;
            }
            case ARRAY: {
                LOGGER.info("Found ARRAY payload = {}", payload);
                Type arrayType = new TypeToken<List<Object>>() {}.getType();
                List<Object> arrayValue = gson.fromJson(payload, arrayType);

                Map<String, Object> processVariables = new HashMap<>();

                determineVariableName(
                        processInstanceExists,
                        resultVariable,
                        fallbackVariable
                ).ifPresent(variableName -> processVariables.put(variableName, arrayValue));

                return processVariables;
            }
            case UNKNOWN: {
                LOGGER.warn("Given payload = {} is unknown! Ignore it!", payload);
            }
            default: {
                return new HashMap<>();
            }
        }
    }

    private void sendSignalWithExecutionId(String topic, ValueTypes valueType, String payload, Optional<String> resultValue,
                                           Optional<String> fallbackValue, String processInstanceId) {
        final Map<String, Object> processVariables = prepareProcessVariables(
                payload,
                valueType,
                true,
                resultValue,
                fallbackValue
        );
        LOGGER.info("Send to execution id (process instance id = {}) a signal with name/topic = {} including process " +
                "variables = {}", processInstanceId, topic, processVariables);
        runtimeService.createSignalEvent(topic)
                .executionId(processInstanceId)
                .setVariables(processVariables)
                .send();
    }

    private void sendSignalWithoutExecutionId(String topic, ValueTypes valueType, String payload,
                                              Optional<String> resultValue) {
        final Map<String, Object> processVariables = prepareProcessVariables(
                payload,
                valueType,
                false,
                resultValue,
                Optional.empty()
        );
        LOGGER.info("Send a signal without execution id with name/topic = {} including process " +
                "variables = {}", topic, processVariables);
        runtimeService.createSignalEvent(topic)
                .setVariables(processVariables)
                .send();
    }

    @Override
    protected void handleMessage(String topic, MqttMessage message) throws HassioException {
        final String payload = message.toString();
        try {
            LOGGER.info("About to handle message for topic = {} with payload = {}", topic, message);
            final ValueTypes valueType = checkValueType(payload);

            if (tempRuntimeSubscriptions.containsKey(topic)) {
                tempRuntimeSubscriptions.get(topic).stream()
                        .forEach(listEntry -> {
                            final String processInstanceId = listEntry.component1();
                            final Optional<String> resultVariable = listEntry.component2();
                            sendSignalWithExecutionId(
                                    topic,
                                    valueType,
                                    payload,
                                    resultVariable,
                                    Optional.of(PluginConsts.EngineListener.EXT_PROP_FALLBACK_VAR_NAME),
                                    processInstanceId
                            );
                        });
            } else {
                LOGGER.info("No tempRuntimeSubscriptions exists. Nothing to do.");
            }

            sendSignalWithoutExecutionId(
                    topic,
                    valueType,
                    payload,
                    Optional.empty()
            );
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
