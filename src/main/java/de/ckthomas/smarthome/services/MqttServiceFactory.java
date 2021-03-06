package de.ckthomas.smarthome.services;

import org.eclipse.paho.client.mqttv3.MqttClient;

/**
 * This Factory provides a service client based on OkHttp3.
 * @author Christian Thomas
 */
public abstract class MqttServiceFactory {

    private static MqttService mqttService = null;

    public static boolean isInstantiated() {
        return mqttService != null;
    }

    public static boolean isNotInstantiated() {
        return !isInstantiated();
    }

    public static MqttService getInstance() {
        return getInstance(null);
    }

    public static MqttService getInstance(String serverURI) {
        return getInstance(null, MqttClient.generateClientId());
    }

    public static MqttService getInstance(String serverURI, String uniqueClientId) {
        if (isNotInstantiated()) {
            mqttService = new MqttService(serverURI, uniqueClientId);
        }
        return mqttService;
    }

}
