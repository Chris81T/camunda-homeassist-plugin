package de.ckthomas.smarthome.camunda.listeners;

import de.ckthomas.smarthome.camunda.PluginConsts;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMqttExecutionListener implements ExecutionListener {

    protected final Logger LOGGER;

    protected final String signalRef;

    private String signalName = null;

    public AbstractMqttExecutionListener(String signalRef, Class<?> listenerClass) {
        LOGGER = LoggerFactory.getLogger(listenerClass);
        this.signalRef = signalRef;
    }

    protected String getSignalName(DelegateExecution execution) {
        if (signalName == null) {
            signalName = execution.getProcessEngineServices()
                    .getRepositoryService()
                    .getBpmnModelInstance(execution.getProcessDefinitionId())
                    .getModelElementById(signalRef)
                    .getAttributeValue(PluginConsts.EngineListener.ELEM_SIGNAL_NAME);
        }

        return signalName;
    }

}
