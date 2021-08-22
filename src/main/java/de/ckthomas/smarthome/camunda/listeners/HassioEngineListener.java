package de.ckthomas.smarthome.camunda.listeners;

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
    public void parseIntermediateSignalCatchEventDefinition(Element signalEventDefinition, ActivityImpl signalActivity) {
        super.parseIntermediateSignalCatchEventDefinition(signalEventDefinition, signalActivity);
    }

}
