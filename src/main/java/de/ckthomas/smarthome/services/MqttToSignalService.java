package de.ckthomas.smarthome.services;

import de.ckthomas.smarthome.exceptions.HassioException;
import org.camunda.bpm.engine.RuntimeService;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author Christian Thomas
 */
public class MqttToSignalService extends AbstractMqttService {

    MqttToSignalService(RuntimeService runtimeService, String serverURI, String username, char[] password,
                        String mqttProcessStartTopic, String uniqueClientId) {
        super(MqttToSignalService.class,
                runtimeService,
                serverURI,
                username,
                password,
                mqttProcessStartTopic,
                uniqueClientId);
    }

    @Override
    protected void handleMessage(MqttMessage message) throws HassioException {

    }
}
