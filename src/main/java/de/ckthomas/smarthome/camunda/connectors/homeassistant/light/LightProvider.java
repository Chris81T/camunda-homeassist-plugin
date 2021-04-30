package de.ckthomas.smarthome.camunda.connectors.homeassistant.light;

import de.ckthomas.smarthome.camunda.connectors.homeassistant.HassioConsts;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.common.CommonProvider;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.switchonoff.SwitchConnector;
import org.camunda.connect.spi.Connector;

/**
 * @author Christian Thomas
 */
public class LightProvider extends CommonProvider {

    public LightProvider() {
        super(LightProvider.class);
    }

    @Override
    public String getConnectorId() {
        return HassioConsts.ConnectorIds.ID_LIGHT;
    }

    @Override
    public Connector<?> createConnectorInstance() {
        LightConnector connector = new LightConnector(getConnectorId(), basePath, authKey, authVal);
        LOGGER.info("Creating new {} instance = {}", getClass().getSimpleName(), connector);
        return connector;
    }

}
