package de.ckthomas.smarthome.camunda.connectors.homeassistant.light;

import de.ckthomas.smarthome.camunda.connectors.homeassistant.HassioConsts;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.common.CommonConnector;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.common.CommonRequest;
import org.camunda.connect.spi.ConnectorResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://www.home-assistant.io/integrations/light
 *
 * service values: turn_on, turn_off
 *
 * turn_on has different optional parameters to control the light. Check out the documentation
 *
 * @author Christian Thomas
 */
public class LightConnector extends CommonConnector {

    /**
     * Every parameter, known from the documentation, will be tracked here. Only that keys are recognized by that
     * connector.
     */
    private final List<String> optionalParamKeys = Arrays.asList(
            "transition",
            "profile",
            "hs_color",
            "xy_color",
            "rgb_color",
            "rgbw_color",
            "rgbww_color",
            "color_temp",
            "kelvin",
            "color_name",
            "brightness",
            "brightness_pct",
            "brightness_step",
            "brightness_step_pct",
            "flash",
            "effect"
    );

    public LightConnector(String connectorId, String basePath, String authKey, String authValue) {
        super(connectorId, basePath, authKey, authValue);
    }

    @Override
    public ConnectorResponse execute(CommonRequest request) {
        Map<String, Object> requestParameters = request.getRequestParameters();
        LOGGER.info("About to execute {} with given request parameters = {}", getClass().getSimpleName(), requestParameters);

        final String service = (String) requestParameters.get(HassioConsts.Common.KEY_URL_SERVICE);
        final String url = createServiceUrl(HassioConsts.Light.DOMAIN, service);

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put(HassioConsts.Light.JSON_BODY_ENTITY_ID, requestParameters.get(HassioConsts.Light.JSON_BODY_ENTITY_ID));

        optionalParamKeys.stream()
                .filter(requestParameters::containsKey)
                .forEach(optionalKey -> jsonMap.put(optionalKey, requestParameters.get(optionalKey)));

        final String jsonBody = toJson(jsonMap);

        return super.perform(request, url, jsonBody);
    }

}
