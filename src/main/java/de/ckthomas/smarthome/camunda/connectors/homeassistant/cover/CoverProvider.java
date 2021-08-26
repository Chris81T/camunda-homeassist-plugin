package de.ckthomas.smarthome.camunda.connectors.homeassistant.cover;

import de.ckthomas.smarthome.camunda.PluginConsts;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.common.CommonProvider;
import org.camunda.connect.spi.Connector;

/**
 * @author Christian Thomas
 */
public class CoverProvider extends CommonProvider {

    public CoverProvider() {
        super(CoverProvider.class);
    }

    @Override
    public String getConnectorId() {
        return PluginConsts.ConnectorIds.ID_COVER;
    }

    @Override
    public Connector<?> createConnectorInstance() {
        CoverConnector connector = new CoverConnector(getConnectorId(), basePath, authKey, authVal);
        LOGGER.info("Creating new {} instance = {}", getClass().getSimpleName(), connector);
        return connector;
    }

}
