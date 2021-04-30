package de.ckthomas.smarthome.camunda.connectors.homeassistant.cover;

import de.ckthomas.smarthome.camunda.connectors.homeassistant.HassioConsts;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.common.CommonProvider;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.switchonoff.SwitchConnector;
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
        return HassioConsts.ConnectorIds.ID_COVER;
    }

    @Override
    public Connector<?> createConnectorInstance() {
        CoverConnector connector = new CoverConnector(getConnectorId(), basePath, authKey, authVal);
        LOGGER.info("Creating new {} instance = {}", getClass().getSimpleName(), connector);
        return connector;
    }

}
