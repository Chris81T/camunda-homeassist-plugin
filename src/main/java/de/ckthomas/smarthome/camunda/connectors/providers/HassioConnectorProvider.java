package de.ckthomas.smarthome.camunda.connectors.providers;

import de.ckthomas.smarthome.camunda.connectors.HassioConnector;
import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorProvider;

public class HassioConnectorProvider implements ConnectorProvider {

    private static final String CONNECTOR_ID = "hassio-connector";

    @Override
    public String getConnectorId() {
        System.out.println("##################### GET CONNECTOR ID.... ");
        return CONNECTOR_ID;
    }

    @Override
    public Connector<?> createConnectorInstance() {
        System.out.println("##################### CREATE CONNECTOR INSTANCE.... ");
        return new HassioConnector(CONNECTOR_ID);
    }

}
