package de.ckthomas.smarthome.camunda.connectors.homeassistant.common;

import de.ckthomas.smarthome.camunda.connectors.homeassistant.HassioConsts;
import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author Christian Thomas
 */
public class CommonProvider implements ConnectorProvider {

    protected final Logger LOGGER;

    protected static final String authKey = System.getProperty(HassioConsts.Common.COMMON_AUTH_KEY);
    protected static final String authVal = System.getProperty(HassioConsts.Common.COMMON_AUTH_VAL);
    protected static final String basePath = System.getProperty(HassioConsts.Common.COMMON_BASE_PATH);

    public CommonProvider() {
        this(CommonProvider.class);
    }

    public CommonProvider(Class<? extends ConnectorProvider> connectorClass) {
        LOGGER = LoggerFactory.getLogger(connectorClass);
        LOGGER.info("While creating connector instance of type {}, following system properties are detected " +
                "(use CATALINA_OPTS to set them). " +
                "{}: {}, {}: {}, {}: {}", Arrays.asList(
                getClass().getSimpleName(),
                HassioConsts.Common.COMMON_AUTH_KEY, authKey,
                HassioConsts.Common.COMMON_AUTH_VAL, authVal.substring(0, 10) + "...",
                HassioConsts.Common.COMMON_BASE_PATH, basePath
        ).toArray());
    }

    @Override
    public String getConnectorId() {
        return HassioConsts.ConnectorIds.ID_COMMON;
    }

    @Override
    public Connector<?> createConnectorInstance() {
        CommonConnector connector = new CommonConnector(getConnectorId(), basePath, authKey, authVal);
        LOGGER.debug("Creating new {} instance = {}", getClass().getSimpleName(), connector);
        return connector;
    }

}
