package de.ckthomas.smarthome.camunda.plugins;

import de.ckthomas.smarthome.camunda.connectors.homeassistant.HassioConsts;
import de.ckthomas.smarthome.exceptions.HassioException;
import de.ckthomas.smarthome.services.ProcessStarterService;
import de.ckthomas.smarthome.services.ProcessStarterServiceFactory;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
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
//        processEngineConfiguration.getCustomPreBPMNParseListeners().forEach(listener -> {
//            listener.parseBoundarySignalEventDefinition();
//        });
    }

    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        LOGGER.info("postInit. processEngineConfiguration = {}", processEngineConfiguration);
    }

    /**
     * To that time the runtime service is available. So instantiate the ProcessStarterService, so it can start its work
     * @param processEngine
     */
    @Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {
        try {
            LOGGER.info("Process-Engine with name = {} is ready for work", processEngine.getName());
            RuntimeService runtimeService = processEngine.getRuntimeService();

            String mqttProcessStartTopic = System.getProperty(HassioConsts.EnginePlugin.MQTT_PROCESS_START_TOPIC);

            if (mqttProcessStartTopic == null) {
                mqttProcessStartTopic = HassioConsts.EnginePlugin.MQTT_PROCESS_START_TOPIC_DEFAULT;
            }

            final String serverURI = System.getProperty(HassioConsts.EnginePlugin.MQTT_SERVER_URI);
            final String username = System.getProperty(HassioConsts.EnginePlugin.MQTT_USERNAME);
            final String password = System.getProperty(HassioConsts.EnginePlugin.MQTT_PASSWORD);

            if (serverURI != null) {
                LOGGER.info("Instantiate ProcessStarterService with following details: runtimeService = {}, " +
                                "mqttStartTopic = {}, " +
                                "serverURI = {}, " +
                                "username = {}, " +
                                "password = {}",
                        new Object[]{runtimeService, mqttProcessStartTopic, serverURI, username, password});
                ProcessStarterService processStarterService = ProcessStarterServiceFactory.getInstance(
                        runtimeService,
                        mqttProcessStartTopic,
                        serverURI,
                        username,
                        password != null ? password.toCharArray() : null
                );

                LOGGER.info("ProcessStarterService = {} is instantiated. About to start it...", processStarterService);

                processStarterService.start();
                LOGGER.info("ProcessStarterService is started.");

            } else {
                final String msg = "Could not start the ProcessStartService, Some missing properties (at least serverURI)!";
                throw new HassioException(msg);
            }
        } catch (Exception e) {
            LOGGER.error("HassioEnginePlugin failed! Service not available!", e);
        }
    }
}
