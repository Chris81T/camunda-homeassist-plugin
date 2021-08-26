package de.ckthomas.smarthome.services;

import org.camunda.bpm.engine.RuntimeService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This Factory provides a service, that will receive mqtt messages and delegates them as signal-events.
 *
 * Every process, that is interested in, can use signal catch events.
 *
 * @author Christian Thomas
 */
public abstract class MqttToSignalServiceFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttToSignalServiceFactory.class);

    /**
     * key is the topic
     * List of string are all avtive/pending executions (processInstanceId:activityInstanceId combination)
     */
    private static final Map<String, List<String>> runtimeSubscriptions = new HashMap<>();

    private static MqttToSignalService mqttToSignalService = null;

    private static MqttToSignalService createInstance(String uniqueClientId, RuntimeService runtimeService, String serverURI,
                                                  String username, char[] password, String... mqttProcessStartTopics) {
        return new MqttToSignalService(
                    runtimeService,
                    serverURI,
                    username,
                    password,
                    uniqueClientId,
                    mqttProcessStartTopics
        );
    }

    public static boolean isInstantiated() {
        return mqttToSignalService != null;
    }

    public static boolean isNotInstantiated() {
        return !isInstantiated();
    }

    public static MqttToSignalService getCurrentInstance() {
        return mqttToSignalService;
    }

    public static MqttToSignalService getInstance(RuntimeService runtimeService, String... mqttProcessStartTopics) {
        return getInstance(runtimeService, null, null, null, mqttProcessStartTopics);
    }

    public static MqttToSignalService getInstance(RuntimeService runtimeService, String serverURI, String username,
                                                  char[] password, String... mqttProcessStartTopics) {
        return getInstance(MqttClient.generateClientId(), runtimeService, serverURI, username, password,
                mqttProcessStartTopics);
    }

    public static MqttToSignalService getInstance(String uniqueClientId, RuntimeService runtimeService, String serverURI,
                                                  String username, char[] password, String... mqttProcessStartTopics) {
        if (isNotInstantiated()) {

            mqttToSignalService = createInstance(
                    uniqueClientId,
                    runtimeService,
                    serverURI,
                    username,
                    password,
                    mqttProcessStartTopics
            );
        }
        return mqttToSignalService;
    }

    private static final String combineInstanceIds(String processInstanceId, String activityInstanceId) {
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
    public static void addRuntimeSubscription(String topic, String processInstanceId, String activityInstanceId) {
        if (isNotInstantiated()) {
            LOGGER.error("Could not add a runtime subscription. No instance of the service is running! Normally during " +
                    "start-up an instance should be created! Abort this operation!");
            return;
        }

        if (!runtimeSubscriptions.containsKey(topic)) {
            getCurrentInstance().subscribe(topic);
            List<String> newList = new ArrayList<>();
            newList.add(combineInstanceIds(processInstanceId, activityInstanceId));
            runtimeSubscriptions.put(topic, newList);
        } else {
            List<String> subscriptors = runtimeSubscriptions.get(topic);
            final String combination = combineInstanceIds(processInstanceId, activityInstanceId);
            if (!subscriptors.contains(combination)) {
                subscriptors.add(combination);
            } else {
                LOGGER.warn("A combination of ProcessInstanceId and ActivityInstanceId already exists?! Value = {}",
                        combination);
            }
        }
    }

    public static void removeRuntimeSubscription(String topic, String processInstanceId, String activityInstanceId) {
        if (isNotInstantiated()) {
            LOGGER.warn("Could not remove a runtime subscription. No instance of the service is running! So nothing to " +
                    "do.");
            return;
        }

        if (runtimeSubscriptions.containsKey(topic)) {
            final String combination = combineInstanceIds(processInstanceId, activityInstanceId);
            List<String> subscriptors = runtimeSubscriptions.get(topic);
            List<String> newList = subscriptors.stream()
                    .filter(combi -> !combi.equals(combination)).collect(Collectors.toList());

            if (newList.isEmpty()) {
                LOGGER.info("Close/remove runtime subscription for topic = {}", topic);
                getCurrentInstance().unsubscribe(topic);
                runtimeSubscriptions.remove(topic);
            } else {
                runtimeSubscriptions.put(topic, newList);
            }
        } else {
            LOGGER.warn("Could not unsubscribe, while given topic = {} is unknown!", topic);
        }

    }

}
