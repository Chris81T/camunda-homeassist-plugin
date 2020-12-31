package de.ckthomas.smarthome.camunda.connectors.responses;

import org.camunda.connect.impl.AbstractConnectorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HassioConnectorResponse extends AbstractConnectorResponse {

    private static final Logger LOGGER = LoggerFactory.getLogger(HassioConnectorResponse.class);

    @Override
    protected void collectResponseParameters(Map<String, Object> responseParameters) {
        LOGGER.info("Collecting response parameters = {}", responseParameters);

    }
}
