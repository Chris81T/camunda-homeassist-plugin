package de.ckthomas.smarthome.camunda.connectors.homeassistant.common;

import org.camunda.connect.impl.AbstractConnectorRequest;
import org.camunda.connect.spi.Connector;

public class CommonRequest extends AbstractConnectorRequest<CommonResponse> {

    public CommonRequest(Connector connector) {
        super(connector);
    }

}
