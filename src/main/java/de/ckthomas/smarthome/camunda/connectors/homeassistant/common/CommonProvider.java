package de.ckthomas.smarthome.camunda.connectors.homeassistant.common;

import de.ckthomas.smarthome.camunda.PluginConsts;
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

    protected static final String authKey = System.getProperty(PluginConsts.Common.AUTH_KEY);
    protected static final String authVal = System.getProperty(PluginConsts.Common.AUTH_VAL);
    protected static final String basePath = System.getProperty(PluginConsts.Common.BASE_PATH);

    public CommonProvider() {
        this(CommonProvider.class);
    }

    public CommonProvider(Class<? extends ConnectorProvider> connectorClass) {
        LOGGER = LoggerFactory.getLogger(connectorClass);
        LOGGER.info("While creating connector instance of type {}, following system properties are detected " +
                "(use CATALINA_OPTS to set them). " +
                "{}: {}, {}: {}, {}: {}", Arrays.asList(
                getClass().getSimpleName(),
                PluginConsts.Common.AUTH_KEY, authKey,
                PluginConsts.Common.AUTH_VAL, authVal.substring(0, 10) + "...",
                PluginConsts.Common.BASE_PATH, basePath
        ).toArray());
    }

    @Override
    public String getConnectorId() {
        return PluginConsts.ConnectorIds.ID_COMMON;
    }

    @Override
    public Connector<?> createConnectorInstance() {
        CommonConnector connector = new CommonConnector(getConnectorId(), basePath, authKey, authVal);
        LOGGER.info("Creating new {} instance = {}", getClass().getSimpleName(), connector);
        return connector;
    }

}
