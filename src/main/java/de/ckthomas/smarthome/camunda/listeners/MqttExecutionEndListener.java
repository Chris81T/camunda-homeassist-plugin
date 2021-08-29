package de.ckthomas.smarthome.camunda.listeners;

import de.ckthomas.smarthome.services.MqttToSignalService;
import de.ckthomas.smarthome.services.MqttToSignalServiceFactory;
import org.camunda.bpm.engine.delegate.DelegateExecution;

/**
 * @author Christian Thomas
 */
public class MqttExecutionEndListener extends AbstractMqttExecutionListener {

    public MqttExecutionEndListener(String signalRef) {
        super(signalRef, MqttExecutionStartListener.class);
        LOGGER.debug("Instantiate Mqtt END Listener = {}", this);
    }

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        final String processInstanceId = execution.getProcessInstanceId();
        final String topic = getSignalName(execution);
        stopListeningToTopic(topic, processInstanceId);
    }

    private void stopListeningToTopic(String topic, String processInstanceId) {
        LOGGER.info("About to stop listening to Mqtt topic = {}, ProcessInstanceId = {}", topic, processInstanceId);
        MqttToSignalService mqttService = MqttToSignalServiceFactory.getCurrentInstance();
        mqttService.removeTempRuntimeSubscription(
                topic,
                processInstanceId
        );
    }

}
