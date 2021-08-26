package de.ckthomas.smarthome.camunda.listeners;

import de.ckthomas.smarthome.camunda.PluginConsts;
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
        handleElementWithActivityImpl(signalEventDefinition, signalActivity);
    }

    @Override
    public void parseIntermediateSignalCatchEventDefinition(Element signalEventDefinition, ActivityImpl signalActivity) {
        super.parseIntermediateSignalCatchEventDefinition(signalEventDefinition, signalActivity);
        handleElementWithActivityImpl(signalEventDefinition, signalActivity);
    }

    private void handleElementWithActivityImpl(Element eventDefinition, ActivityImpl activity) {
        final String signalRef = eventDefinition.attribute(PluginConsts.EngineListener.ELEM_SIGNAL_REF);
        addExecutionListener(activity, new MqttExecutionStartListener(signalRef), ExecutionListener.EVENTNAME_START);
        addExecutionListener(activity, new MqttExecutionEndListener(signalRef), ExecutionListener.EVENTNAME_END);
    }

    private void addExecutionListener(ActivityImpl activity, ExecutionListener executionListener, String eventName) {
        activity.addListener(eventName, executionListener);
    }

}
