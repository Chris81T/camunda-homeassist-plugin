package de.ckthomas.smarthome.camunda.listeners;

import de.ckthomas.smarthome.camunda.PluginConsts;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;

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

    protected Optional<String> getResultVariableName(DelegateExecution execution) {
        Collection<CamundaProperty> extensionProperties = execution.getBpmnModelElementInstance()
                .getExtensionElements()
                .getElementsQuery()
                .filterByType(CamundaProperties.class)
                .singleResult()
                .getCamundaProperties();

        return extensionProperties.stream()
                .filter(camundaProperty -> PluginConsts
                        .EngineListener
                        .EXT_PROP_RESULT_VAR_NAME.equals(camundaProperty.getCamundaName()))
                .findFirst()
                .map(camundaProperty -> camundaProperty.getCamundaValue());
    }

}
