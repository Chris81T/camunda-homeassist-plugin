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
        System.out.println("##################### CREATE REQUEST....");
        return new HassioConnectorRequest(this);
    }

    @Override
    public ConnectorResponse execute(HassioConnectorRequest request) {
        System.out.println("##################### EXECUTE.... " + request);

        System.out.println("REQ PARAMS: " + request.getRequestParameters());

        HassioConnectorResponse response = new HassioConnectorResponse();

        response.getResponseParameters().put("respone", "Das soll einfach mal ein Ergebnis sein...");

        return response;
    }
}
