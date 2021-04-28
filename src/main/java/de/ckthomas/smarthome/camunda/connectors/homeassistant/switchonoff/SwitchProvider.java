package de.ckthomas.smarthome.camunda.connectors.homeassistant.switchonoff;

import de.ckthomas.smarthome.camunda.connectors.homeassistant.HassioConsts;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.common.CommonProvider;
import org.camunda.connect.spi.Connector;

import java.util.Arrays;

/**
 * @author Christian Thomas
 */
public class SwitchProvider extends CommonProvider {

    public SwitchProvider() {
        super(SwitchProvider.class);
    }

    @Override
    public String getConnectorId() {
        return HassioConsts.ConnectorIds.ID_SWITCH;
    }

    @Override
    public Connector<?> createConnectorInstance() {
        SwitchConnector connector = new SwitchConnector(getConnectorId(), basePath, authKey, authVal);
        LOGGER.info("Creating new {} instance = {}", getClass().getSimpleName(), connector);
        return connector;
    }

}
