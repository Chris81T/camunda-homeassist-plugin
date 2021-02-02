package de.ckthomas.smarthome.camunda.connectors.homeassistant.common;

import org.camunda.connect.impl.AbstractConnectorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class CommonResponse extends AbstractConnectorResponse {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonResponse.class);

    @Override
    protected void collectResponseParameters(Map<String, Object> responseParameters) {

    }
}
