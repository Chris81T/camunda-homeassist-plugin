package de.ckthomas.smarthome.services;

import de.ckthomas.smarthome.camunda.connectors.homeassistant.HassioConsts;
import org.camunda.bpm.engine.RuntimeService;
import org.eclipse.paho.client.mqttv3.MqttClient;

/**
 * This Factory provides a service, that provides process starts over mqtt
 * @author Christian Thomas
 */
public abstract class ProcessStarterServiceFactory {

    private static ProcessStarterService processStarterService = null;

    public static boolean isInstantiated() {
        return processStarterService != null;
    }

    public static boolean isNotInstantiated() {
        return !isInstantiated();
    }

    public static ProcessStarterService getInstance() {
        return getInstance(null, null);
    }

    public static ProcessStarterService getInstance(RuntimeService runtimeService, String mqttProcessStartTopic) {
        return getInstance(runtimeService, mqttProcessStartTopic, null, null, null);
    }

    public static ProcessStarterService getInstance(RuntimeService runtimeService, String mqttProcessStartTopic,
                                                    String serverURI, String username, char[] password) {
        return getInstance(runtimeService, mqttProcessStartTopic, serverURI, username, password,
                MqttClient.generateClientId());
    }

    public static ProcessStarterService getInstance(RuntimeService runtimeService, String mqttProcessStartTopic,
                                                    String serverURI, String username, char[] password,
                                                    String uniqueClientId) {
        if (isNotInstantiated()) {

            if (mqttProcessStartTopic == null) {
                mqttProcessStartTopic = HassioConsts.EnginePlugin.MQTT_PROCESS_START_TOPIC_DEFAULT;
            }

            processStarterService = new ProcessStarterService(
                    runtimeService,
                    serverURI,
                    username,
                    password,
                    uniqueClientId,
                    mqttProcessStartTopic
            );
        }
        return processStarterService;
    }

}
