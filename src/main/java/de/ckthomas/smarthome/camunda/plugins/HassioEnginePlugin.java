package de.ckthomas.smarthome.camunda.plugins;

import de.ckthomas.smarthome.camunda.PluginConsts;
import de.ckthomas.smarthome.camunda.listeners.HassioEngineListener;
import de.ckthomas.smarthome.exceptions.HassioException;
import de.ckthomas.smarthome.services.MqttToSignalService;
import de.ckthomas.smarthome.services.MqttToSignalServiceFactory;
import de.ckthomas.smarthome.services.ProcessStarterService;
import de.ckthomas.smarthome.services.ProcessStarterServiceFactory;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Thomas
 */
public class HassioEnginePlugin extends AbstractProcessEnginePlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(HassioEnginePlugin.class);

    private final HassioEngineListener hassioEngineListener = new HassioEngineListener();

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        LOGGER.info("PRE-INIT Phase. About to initiate the MqttService...");
        List<BpmnParseListener> listeners = processEngineConfiguration.getCustomPreBPMNParseListeners();

        if (listeners == null) {
            listeners = new ArrayList<>();
        }

        listeners.add(hassioEngineListener);

        processEngineConfiguration.setCustomPreBPMNParseListeners(listeners);
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

            final String serverURI = System.getProperty(PluginConsts.EnginePlugin.MQTT_SERVER_URI);
            final String username = System.getProperty(PluginConsts.EnginePlugin.MQTT_USERNAME);
            final String password = System.getProperty(PluginConsts.EnginePlugin.MQTT_PASSWORD);

            initProcessStarterService(serverURI, username, password, runtimeService);
            initMqttToSignalService(serverURI, username, password, runtimeService);

        } catch (Exception e) {
            LOGGER.error("HassioEnginePlugin failed! Services not available!", e);
        }
    }

    private void initProcessStarterService(String serverURI, String username, String password,
                                           RuntimeService runtimeService) throws Exception {

        if (serverURI != null) {
            String mqttProcessStartTopic = System.getProperty(PluginConsts.EnginePlugin.MQTT_PROCESS_START_TOPIC);

            LOGGER.info("Instantiate ProcessStarterService with following details: runtimeService = {}, " +
                            "mqttStartTopic = {}, " +
                            "serverURI = {}, " +
                            "username = {}, " +
                            "password = ******",
                    runtimeService, mqttProcessStartTopic, serverURI, username, password);

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
    }

    private void initMqttToSignalService(String serverURI, String username, String password,
                                           RuntimeService runtimeService) throws Exception {
        if (serverURI != null) {
            String mqttToBpmnSignalTopic = System.getProperty(PluginConsts.EnginePlugin.MQTT_TO_BPMN_SIGNAL_TOPIC);
            String[] extractedTopics = extractTopics(mqttToBpmnSignalTopic);

            LOGGER.info("Instantiate MqttToSignalService with following details: runtimeService = {}, " +
                            "mqttToBpmnSignalTopic = {}, " +
                            "extractedTopics = {}, " +
                            "serverURI = {}, " +
                            "username = {}, " +
                            "password = {}",
                    runtimeService, mqttToBpmnSignalTopic, extractedTopics, serverURI, username, password);

            MqttToSignalService mqttToSignalService = MqttToSignalServiceFactory.getInstance(
                    runtimeService,
                    serverURI,
                    username,
                    password != null ? password.toCharArray() : null,
                    extractedTopics
            );

            LOGGER.info("MqttToSignalService = {} is instantiated. About to start it...", mqttToSignalService);

            mqttToSignalService.start();
            LOGGER.info("MqttToSignalService is started.");

        } else {
            final String msg = "Could not start the MqttToSignalService, Some missing properties (at least serverURI)!";
            throw new HassioException(msg);
        }
    }

    /**
     * As separator a ',' will be used.
     *
     * Example: "topic/one/a,topic/one/b,topic/two"
     */
    private String[] extractTopics(String topics) {
        final String separator = PluginConsts.EnginePlugin.MQTT_TOPIC_SEPERATOR;

        if (topics.contains(separator)) {
            return topics.split(separator);
        }

        String[] oneTopic = {topics};
        return oneTopic;
    }
}
