package de.ckthomas.smarthome.camunda.connectors.homeassistant.switchonoff;

import de.ckthomas.smarthome.camunda.PluginConsts;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.common.CommonConnector;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.common.CommonRequest;
import org.camunda.connect.spi.ConnectorResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * https://www.home-assistant.io/integrations/switch
 *
 * service values: turn_on, turn_off, toggle
 *
 * @author Christian Thomas
 */
public class SwitchConnector extends CommonConnector {

    public SwitchConnector(String connectorId, String basePath, String authKey, String authValue) {
        super(connectorId, basePath, authKey, authValue);
    }

    @Override
    public ConnectorResponse execute(CommonRequest request) {
        Map<String, Object> requestParameters = request.getRequestParameters();
        LOGGER.info("About to execute {} with given request parameters = {}", getClass().getSimpleName(), requestParameters);

        final String service = (String) requestParameters.get(PluginConsts.Common.KEY_URL_SERVICE);
        final String url = createServiceUrl(PluginConsts.Switch.DOMAIN, service);

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put(PluginConsts.Switch.JSON_BODY_ENTITY_ID, requestParameters.get(PluginConsts.Switch.JSON_BODY_ENTITY_ID));
        final String jsonBody = toJson(jsonMap);

        return super.perform(request, url, jsonBody);
    }

}
