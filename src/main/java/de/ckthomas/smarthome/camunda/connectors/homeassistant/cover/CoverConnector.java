package de.ckthomas.smarthome.camunda.connectors.homeassistant.cover;

import de.ckthomas.smarthome.camunda.connectors.homeassistant.HassioConsts;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.common.CommonConnector;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.common.CommonRequest;
import org.camunda.connect.spi.ConnectorResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * https://www.home-assistant.io/integrations/cover
 *
 * service values: open_cover, stop_cover, close_cover
 *
 * @author Christian Thomas
 */
public class CoverConnector extends CommonConnector {

    public CoverConnector(String connectorId, String basePath, String authKey, String authValue) {
        super(connectorId, basePath, authKey, authValue);
    }

    @Override
    public ConnectorResponse execute(CommonRequest request) {
        Map<String, Object> requestParameters = request.getRequestParameters();
        LOGGER.info("About to execute {} with given request parameters = {}", getClass().getSimpleName(), requestParameters);

        final String service = (String) requestParameters.get(HassioConsts.Common.KEY_URL_SERVICE);
        final String url = createServiceUrl(HassioConsts.Cover.DOMAIN, service);

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put(HassioConsts.Cover.JSON_BODY_ENTITY_ID, requestParameters.get(HassioConsts.Cover.JSON_BODY_ENTITY_ID));
        final String jsonBody = toJson(jsonMap);

        return super.perform(request, url, jsonBody);
    }

}
