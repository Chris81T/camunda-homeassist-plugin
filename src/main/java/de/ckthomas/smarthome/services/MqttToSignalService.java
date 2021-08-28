package de.ckthomas.smarthome.services;

import com.google.gson.reflect.TypeToken;
import de.ckthomas.smarthome.exceptions.HassioException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.SignalEventReceivedBuilder;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Christian Thomas
 */
public class MqttToSignalService extends AbstractMqttService {

    private Optional<String> resultVariable = Optional.empty();

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

            signalEventBuilder.send();

        } catch (Exception e) {
            throw new HassioException("Could not handle MqttMessage to transfer it to a bpmn signal! Payload = " +
                    payload, e);
        }
    }

    public void setResultVariableForNonJsonMessages(Optional<String> resultVariable) {
        this.resultVariable = resultVariable;
    }
}
