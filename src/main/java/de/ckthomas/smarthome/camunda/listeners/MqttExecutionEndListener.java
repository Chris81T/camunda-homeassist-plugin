package de.ckthomas.smarthome.camunda.listeners;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

/**
 * @author Christian Thomas
 */
public class MqttExecutionEndListener extends AbstractMqttExecutionListener {

    public MqttExecutionEndListener(String signalName) {
        super(signalName, MqttExecutionStartListener.class);
    }

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        final String executionId = execution.getId();
        final String activityInstanceId = execution.getActivityInstanceId();
    }

}
