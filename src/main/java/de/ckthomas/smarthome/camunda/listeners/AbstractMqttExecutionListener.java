package de.ckthomas.smarthome.camunda.listeners;

import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMqttExecutionListener implements ExecutionListener {

    protected final Logger LOGGER;

    protected final String signalName;

    public AbstractMqttExecutionListener(String signalName, Class<?> listenerClass) {
        LOGGER = LoggerFactory.getLogger(listenerClass);
        this.signalName = signalName;
    }

}
