package de.ckthomas.smarthome.camunda.listeners;

import de.ckthomas.smarthome.services.MqttToSignalServiceFactory;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

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
        final String activityInstanceId = execution.getActivityInstanceId();
        final String topic = getSignalName(execution);
        stopListeningToTopic(processInstanceId, activityInstanceId, topic);
    }

    private void stopListeningToTopic(String topic, String processInstanceId, String activityInstanceId) {
        LOGGER.info("About to stop listening to Mqtt topic = {}, ProcessInstanceId = {}, " +
                "ActivityInstanceID = {}", topic, processInstanceId, activityInstanceId);
        MqttToSignalServiceFactory.removeRuntimeSubscription(topic, processInstanceId, activityInstanceId);
    }

}
