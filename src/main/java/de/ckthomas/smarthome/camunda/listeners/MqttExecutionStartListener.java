package de.ckthomas.smarthome.camunda.listeners;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

/**
 * @author Christian Thomas
 */
public class MqttExecutionStartListener extends AbstractMqttExecutionListener {

    public MqttExecutionStartListener(String signalName) {
        super(signalName, MqttExecutionStartListener.class);
        LOGGER.info("<> <> <> <> INSTANTIATE START LISTENER {}", this);
    }

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        final String executionId = execution.getId();
        final String activityInstanceId = execution.getActivityInstanceId();

        final String currentActivityId = execution.getCurrentActivityId();

        String foundSignalName = execution.getProcessEngineServices()
                .getRepositoryService()
                .getBpmnModelInstance(execution.getProcessDefinitionId())
                .getModelElementById(signalName)
                .getAttributeValue("name");

        LOGGER.info("[][][][][][] foundSignalName = {}", foundSignalName);

        LOGGER.info("<> <> <> <> <> MQTT START LISTENER {}, {}, {}", executionId, activityInstanceId, currentActivityId);
    }

}
