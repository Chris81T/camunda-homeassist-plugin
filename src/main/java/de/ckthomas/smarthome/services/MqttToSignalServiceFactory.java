package de.ckthomas.smarthome.services;

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

    public static MqttToSignalService getInstance() {
        return getInstance(null);
    }

    public static MqttToSignalService getInstance(String serverURI) {
        return getInstance(null, MqttClient.generateClientId());
    }

    public static MqttToSignalService getInstance(String serverURI, String uniqueClientId) {
        if (isNotInstantiated()) {
          //  mqttService = new MqttService(serverURI, uniqueClientId);
        }
        return mqttToSignalService;
    }

}
