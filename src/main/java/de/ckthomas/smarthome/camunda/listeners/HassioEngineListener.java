package de.ckthomas.smarthome.camunda.listeners;

import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.util.xml.Element;

/**
 * @author Christian Thomas
 */
public class HassioEngineListener extends AbstractBpmnParseListener {

    @Override
    public void parseServiceTask(Element serviceTaskElement, ScopeImpl scope, ActivityImpl activity) {
        super.parseServiceTask(serviceTaskElement, scope, activity);
    }

}
