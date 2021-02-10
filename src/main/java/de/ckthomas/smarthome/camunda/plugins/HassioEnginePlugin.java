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

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        LOGGER.info("PRE-INIT Phase. About to initiate the MqttService...");
    }

    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        LOGGER.info("postInit. processEngineConfiguration = {}", processEngineConfiguration);
    }

    @Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {
        LOGGER.info("Process-Engine with name = {} is ready for work", processEngine.getName());
    }
}
