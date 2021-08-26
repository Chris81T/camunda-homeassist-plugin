package de.ckthomas.smarthome.camunda.listeners;

import de.ckthomas.smarthome.services.MqttToSignalServiceFactory;
import org.camunda.bpm.engine.delegate.DelegateExecution;

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
        final String activityInstanceId = execution.getActivityInstanceId();
        final String topic = getSignalName(execution);
        startListeningToTopic(topic, processInstanceId, activityInstanceId);
    }

    private void startListeningToTopic(String topic, String processInstanceId, String activityInstanceId) {
        LOGGER.info("About to start listening over factory to Mqtt topic = {}, ProcessInstanceId = {}, " +
                "ActivityInstanceID = {}", topic, processInstanceId, activityInstanceId);
        MqttToSignalServiceFactory.addRuntimeSubscription(topic, processInstanceId, activityInstanceId);
    }

}
