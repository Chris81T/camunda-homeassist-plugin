package de.ckthomas.smarthome.camunda.listeners;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

/**
 * @author Christian Thomas
 */
public class MqttExecutionEndListener extends AbstractMqttExecutionListener {

    public MqttExecutionEndListener(String signalName) {
        super(signalName, MqttExecutionStartListener.class);
        LOGGER.info("<> <> <> <> INSTANTIATE END LISTENER {}", this);
    }

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        final String executionId = execution.getId();
        final String activityInstanceId = execution.getActivityInstanceId();
        final String currentActivityId = execution.getCurrentActivityId();

        LOGGER.info("<> <> <> <> <> MQTT END LISTENER {}, {}, {}", executionId, activityInstanceId, currentActivityId);
    }

}
