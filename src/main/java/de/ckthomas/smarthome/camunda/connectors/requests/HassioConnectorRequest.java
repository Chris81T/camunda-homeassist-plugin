package de.ckthomas.smarthome.camunda.connectors.requests;

import de.ckthomas.smarthome.camunda.connectors.responses.HassioConnectorResponse;
import org.camunda.connect.impl.AbstractConnectorRequest;
import org.camunda.connect.spi.Connector;

public class HassioConnectorRequest extends AbstractConnectorRequest<HassioConnectorResponse> {

    public HassioConnectorRequest(Connector connector) {
        super(connector);
    }
}
