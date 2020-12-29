package de.ckthomas.smarthome.camunda.plugins;

import de.ckthomas.smarthome.camunda.listeners.HassioEngineListener;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Thomas
 */
public class HassioEnginePlugin extends AbstractProcessEnginePlugin {

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        List<BpmnParseListener> listeners = processEngineConfiguration.getCustomPreBPMNParseListeners();
        if (listeners == null) {
            listeners = new ArrayList<>();
            processEngineConfiguration.setCustomPreBPMNParseListeners(listeners);
        }
        listeners.add(new HassioEngineListener());
    }

    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super.postInit(processEngineConfiguration);
    }

    @Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {
        super.postProcessEngineBuild(processEngine);
    }
}
