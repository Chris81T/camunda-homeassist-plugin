package de.ckthomas.smarthome.camunda.listeners;

import de.ckthomas.smarthome.services.MqttToSignalService;
import de.ckthomas.smarthome.services.MqttToSignalServiceFactory;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import java.util.Optional;

/**
 * @author Christian Thomas
 */
public class MqttExecutionStartListener extends AbstractMqttExecutionListener {

    public MqttExecutionStartListener(String signalRef) {
        super(signalRef, MqttExecutionStartListener.class);
        LOGGER.debug("Instantiate Mqtt START Listener = {}", this);
    }

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        final String processInstanceId = execution.getProcessInstanceId();
        final String topic = getSignalName(execution);
        final Optional<String> resultVariable = getResultVariableName(execution);
        startListeningToTopic(topic, processInstanceId, resultVariable);
    }

    private void startListeningToTopic(String topic, String processInstanceId, Optional<String> resultVariable) {
        LOGGER.info("About to start listening over factory to Mqtt topic = {}, ProcessInstanceId = {}, " +
                "resultVariable = {}", topic, processInstanceId, resultVariable);

        MqttToSignalService mqttService = MqttToSignalServiceFactory.getCurrentInstance();
        mqttService.addTempRuntimeSubscription(
                topic,
                processInstanceId,
                resultVariable
        );
    }

}
