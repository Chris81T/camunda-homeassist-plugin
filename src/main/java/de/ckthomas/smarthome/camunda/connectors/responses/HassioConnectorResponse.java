package de.ckthomas.smarthome.camunda.connectors.responses;

import org.camunda.connect.impl.AbstractConnectorResponse;

import java.util.Map;

public class HassioConnectorResponse extends AbstractConnectorResponse {

    @Override
    protected void collectResponseParameters(Map<String, Object> responseParameters) {
        System.out.println("##################### COLLECT RESPONSE PARAMS.... " + responseParameters);

    }
}
