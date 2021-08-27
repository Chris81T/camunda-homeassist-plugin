package de.ckthomas.smarthome.services;

import org.camunda.bpm.engine.RuntimeService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
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
     * key is a processInstanceId:activityInstanceId combination
     */
    private static final Map<String, MqttToSignalService> runtimeMqttServices = new HashMap<>();

    private static MqttToSignalService mqttToSignalService = null;

    private static String serverURI = null;
    private static String username = null;
    private static char[] password = null;

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

    public static void setConnectionDetailsGlobally(String serverURI, String username, char[] password) {
        MqttToSignalServiceFactory.serverURI = serverURI;
        MqttToSignalServiceFactory.username = username;
        MqttToSignalServiceFactory.password = password;
    }

    public static MqttToSignalService getInstance(RuntimeService runtimeService, String... mqttProcessStartTopics) {
        return getInstance(runtimeService, serverURI, username, password, mqttProcessStartTopics);
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
    public static void constructRuntimeSubscription(String topic, String processInstanceId, String activityInstanceId,
                                                    RuntimeService runtimeService, Optional<String> resultVariable) {

        final String combination = combineInstanceIds(processInstanceId, activityInstanceId);
        if (!runtimeMqttServices.containsKey(combination)) {
            final MqttToSignalService newInstance = getInstance(runtimeService, topic);
            newInstance.setResultVariableForNonJsonMessages(resultVariable);
            runtimeMqttServices.put(combination, newInstance);
        } else {
            LOGGER.warn("MqttToSignalService for id = {} already exist! Do nothing!", combination);
        }
    }

    public static void destructRuntimeSubscription(String topic, String processInstanceId, String activityInstanceId) {
        final String combination = combineInstanceIds(processInstanceId, activityInstanceId);

        if (runtimeMqttServices.containsKey(combination)) {
            MqttToSignalService runningInstance = runtimeMqttServices.remove(combination);
            runningInstance.unsubscribe(topic);
        } else {
            LOGGER.warn("Could not unsubscribe/destruct, while given id = {} is unknown! Do nothing!", combination);
        }
    }

}
