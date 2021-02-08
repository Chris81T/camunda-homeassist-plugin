package de.ckthomas.smarthome.camunda.connectors.homeassistant.switchonoff;

import camundajar.impl.scala.util.parsing.json.JSONObject;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.HassioConsts;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.common.CommonConnector;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.common.CommonRequest;
import org.camunda.connect.spi.ConnectorResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Christian Thomas
 */
public class SwitchConnector extends CommonConnector {

    public SwitchConnector(String connectorId, String basePath, String authKey, String authValue) {
        super(connectorId, basePath, authKey, authValue);
    }

    @Override
    public ConnectorResponse execute(CommonRequest request) {
        Map<String, Object> requestParameters = request.getRequestParameters();
        Map<String, String> map = new HashMap<>();
        map.put(HassioConsts.Switch.SWITCH_KEY_ENTITY_ID,
                (String) requestParameters.get(HassioConsts.Switch.SWITCH_KEY_ENTITY_ID));
        final String jsonBody = toJson(map);
        return super.perform(request, jsonBody);
    }

}
