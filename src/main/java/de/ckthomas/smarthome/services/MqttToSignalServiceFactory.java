package de.ckthomas.smarthome.services;

import de.ckthomas.smarthome.camunda.connectors.homeassistant.HassioConsts;
import org.camunda.bpm.engine.RuntimeService;
import org.eclipse.paho.client.mqttv3.MqttClient;

/**
 * This Factory provides a service, that will receive mqtt messages and delegates them as signal-events.
 *
 * Every process, that is interested in, can use signal catch events.
 *
 * @author Christian Thomas
 */
public abstract class MqttToSignalServiceFactory {

    private static MqttToSignalService mqttToSignalService = null;

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

            mqttToSignalService = new MqttToSignalService(
                    runtimeService,
                    serverURI,
                    username,
                    password,
                    uniqueClientId,
                    mqttProcessStartTopics
            );
        }
        return mqttToSignalService;
    }

}
