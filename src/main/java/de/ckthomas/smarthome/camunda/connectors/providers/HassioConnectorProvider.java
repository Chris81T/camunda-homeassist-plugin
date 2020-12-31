package de.ckthomas.smarthome.camunda.connectors.providers;

import de.ckthomas.smarthome.camunda.connectors.HassioConnector;
import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HassioConnectorProvider implements ConnectorProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(HassioConnectorProvider.class);
    private static final String CONNECTOR_ID = "hassio-connector";

    @Override
    public String getConnectorId() {
        LOGGER.info("Return connector id = '{}'", CONNECTOR_ID);
        return CONNECTOR_ID;
    }

    @Override
    public Connector<?> createConnectorInstance() {
        HassioConnector connector = new HassioConnector(CONNECTOR_ID);
        LOGGER.info("Creating new connector instance = {}", connector);
        return connector;
    }

}
