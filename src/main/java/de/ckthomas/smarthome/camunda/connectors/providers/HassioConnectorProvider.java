package de.ckthomas.smarthome.camunda.connectors.providers;

import de.ckthomas.smarthome.camunda.connectors.HassioConnector;
import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorProvider;

public class HassioConnectorProvider implements ConnectorProvider {

    private static final String CONNECTOR_ID = "hassio-connector";

    @Override
    public String getConnectorId() {
        return CONNECTOR_ID;
    }

    @Override
    public Connector<?> createConnectorInstance() {
        return new HassioConnector(CONNECTOR_ID);
    }

}
