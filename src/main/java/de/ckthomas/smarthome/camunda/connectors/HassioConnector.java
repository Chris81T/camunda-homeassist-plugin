package de.ckthomas.smarthome.camunda.connectors;

import de.ckthomas.smarthome.camunda.connectors.requests.HassioConnectorRequest;
import de.ckthomas.smarthome.camunda.connectors.responses.HassioConnectorResponse;
import org.camunda.connect.impl.AbstractConnector;
import org.camunda.connect.spi.ConnectorResponse;

public class HassioConnector extends AbstractConnector<HassioConnectorRequest, HassioConnectorResponse> {

    public HassioConnector(String connectorId) {
        super(connectorId);
    }

    @Override
    public HassioConnectorRequest createRequest() {
        return null;
    }

    @Override
    public ConnectorResponse execute(HassioConnectorRequest request) {
        return null;
    }
}
