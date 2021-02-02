package de.ckthomas.smarthome.camunda.connectors.homeassistant.common;

import de.ckthomas.smarthome.camunda.connectors.HassioConnector;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.HassioConsts;
import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonProvider implements ConnectorProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonProvider.class);

    @Override
    public String getConnectorId() {
        return HassioConsts.ConnectorIds.ID_COMMON;
    }

    @Override
    public Connector<?> createConnectorInstance() {
        HassioConnector connector = new HassioConnector(getConnectorId());
        LOGGER.debug("Creating new {} instance = {}", getClass().getSimpleName(), connector);
        return connector;
    }

}
