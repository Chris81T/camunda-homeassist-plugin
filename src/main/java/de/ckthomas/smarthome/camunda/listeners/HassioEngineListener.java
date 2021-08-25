package de.ckthomas.smarthome.camunda.listeners;

import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Christian Thomas
 */
public class HassioEngineListener extends AbstractBpmnParseListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(HassioEngineListener.class);

    @Override
    public void parseBoundarySignalEventDefinition(Element signalEventDefinition, boolean interrupting, ActivityImpl signalActivity) {
        super.parseBoundarySignalEventDefinition(signalEventDefinition, interrupting, signalActivity);
    }

    @Override
    public void parseIntermediateSignalCatchEventDefinition(Element signalEventDefinition, ActivityImpl signalActivity) {
        super.parseIntermediateSignalCatchEventDefinition(signalEventDefinition, signalActivity);

        final String signalRef = signalEventDefinition.attribute("signalRef");

        signalEventDefinition.elements().forEach(element -> LOGGER.info(">>>> FOREACH ELEMENT = {}", element));

        signalEventDefinition.attributes().forEach(attribute -> LOGGER.info(">>>> ATTR = {}, attribute() = {}", attribute,
                signalEventDefinition.attribute(attribute)));


        signalActivity.getParentFlowScopeActivity();



        LOGGER.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX signalRef = {}", signalRef);

        addExecutionListener(signalActivity, new MqttExecutionStartListener(signalRef), ExecutionListener.EVENTNAME_START);
        addExecutionListener(signalActivity, new MqttExecutionEndListener(signalRef), ExecutionListener.EVENTNAME_END);
    }

    private void addExecutionListener(ActivityImpl activity, ExecutionListener executionListener, String eventName) {
        activity.addListener(eventName, executionListener);
    }

}
