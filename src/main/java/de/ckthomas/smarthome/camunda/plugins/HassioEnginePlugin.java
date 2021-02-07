package de.ckthomas.smarthome.camunda.plugins;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Christian Thomas
 */
public class HassioEnginePlugin extends AbstractProcessEnginePlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(HassioEnginePlugin.class);

    HassioEnginePlugin() {
        LOGGER.info("About to instantiate HassioEnginePlugin...");
    }

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super.preInit(processEngineConfiguration);
        LOGGER.info("preInit. processEngineConfiguration = {}", processEngineConfiguration);
    }

    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super.postInit(processEngineConfiguration);
        LOGGER.info("postInit. processEngineConfiguration = {}", processEngineConfiguration);
    }

    @Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {
        super.postProcessEngineBuild(processEngine);
        LOGGER.info("Process-Engine with name = {} is ready for work", processEngine.getName());
    }
}
