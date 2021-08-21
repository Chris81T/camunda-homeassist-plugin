package de.ckthomas.smarthome.services;

import com.google.gson.Gson;
import de.ckthomas.smarthome.dtos.ProcessStartDto;
import de.ckthomas.smarthome.exceptions.HassioException;
import org.camunda.bpm.engine.RuntimeService;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author Christian Thomas
 */
public class ProcessStarterService extends AbstractMqttService {

    ProcessStarterService(RuntimeService runtimeService, String serverURI, String username, char[] password,
                          String uniqueClientId, String mqttProcessStartTopic) {
        super(ProcessStarterService.class,
                runtimeService,
                serverURI,
                username,
                password,
                uniqueClientId,
                mqttProcessStartTopic);
    }

    /**
     * Falls es ausgelagert wird, kann dies hier eine abstrakte Methode werden und dann spezifisch umgesetzt werden
     *
     * @param message
     */
    protected void handleMessage(String topic, MqttMessage message) throws HassioException {
        final String payload = message.toString();
        try {
            LOGGER.info("Incoming payload = {} to start new process instance", payload);
            final ProcessStartDto dto = gson.fromJson(payload, ProcessStartDto.class);
            LOGGER.info("Parsed dto from payload = {}", dto);
            if (dto.getBusinessKey() != null) {
                runtimeService.startProcessInstanceByKey(dto.getProcessDefinitionKey(), dto.getBusinessKey(),
                        dto.getVariables());
            } else {
                runtimeService.startProcessInstanceByKey(dto.getProcessDefinitionKey(), dto.getVariables());
            }
        } catch (Exception e) {
            throw new HassioException("Could not start process with key = " + payload, e);
        }
    }

}
