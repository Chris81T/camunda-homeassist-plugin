package de.ckthomas.smarthome.camunda.connectors.homeassistant.common;

import com.google.gson.Gson;
import de.ckthomas.smarthome.camunda.connectors.homeassistant.HassioConsts;
import de.ckthomas.smarthome.exceptions.HassioException;
import de.ckthomas.smarthome.services.RestService;
import de.ckthomas.smarthome.services.RestServiceFactory;
import org.camunda.connect.impl.AbstractConnector;
import org.camunda.connect.spi.ConnectorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @author Christian Thomas
 */
public class CommonConnector extends AbstractConnector<CommonRequest, CommonResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonConnector.class);

    private final String basePath;
    private final String authKey;
    private final String authValue;

    private final Gson gson = new Gson();

    public CommonConnector(String connectorId, String basePath, String authKey, String authValue) {
        super(connectorId);
        this.basePath = basePath;
        this.authKey = authKey;
        this.authValue = authValue;
    }

    @Override
    public CommonRequest createRequest() {
        LOGGER.debug("Creating CommonConnector-Request");
        return new CommonRequest(this);
    }

    private String checkParam(String value, String key, Map<String, Object> requestParameters) {
        LOGGER.debug("check param {} with given value = {}. Will be overwritten, if key is part of request-params",
                key, value);
        return requestParameters.containsKey(key) ? (String) requestParameters.get(key) : value;
    }

    /**
     * Useful helper method for sub-implementations
     *
     * @param map - key - value pair content for the producing json
     * @return json as string
     */
    protected String toJson(Map<String, String> map) {
        return gson.toJson(map);
    }

    protected ConnectorResponse perform(CommonRequest request, String jsonBody) {
        try {
            Map<String, Object> requestParameters = request.getRequestParameters();
            LOGGER.info("Executing operation. Given request = {}, given request parameters = {}", request,
                    requestParameters);

            final String url = (String) requestParameters.get(HassioConsts.Common.COMMON_KEY_URL);

            final String basePath = checkParam(this.basePath, HassioConsts.Common.COMMON_BASE_PATH, requestParameters);
            final String authKey = checkParam(this.authKey, HassioConsts.Common.COMMON_AUTH_KEY, requestParameters);
            final String authValue = checkParam(this.authValue, HassioConsts.Common.COMMON_AUTH_VAL, requestParameters);

            RestService service = RestServiceFactory.getInstance(basePath, authKey, authValue);

            service.execute(url, jsonBody);

            CommonResponse response = new CommonResponse();
            return response;
        } catch (IOException e) {
            LOGGER.error("Something went wrong during service execution!", e);
            throw new HassioException("Could not perform execution. IOException (Service Call). Error Message = " +
                    e.getMessage(), e);
        }   catch (Exception e) {
            LOGGER.error("Something went wrong during execution!", e);
            throw new HassioException("Could not perform execution. Error Message = " + e.getMessage(), e);
        }
    }

    @Override
    public ConnectorResponse execute(CommonRequest request) {
        Map<String, Object> requestParameters = request.getRequestParameters();
        final String jsonBody = (String) requestParameters.get(HassioConsts.Common.COMMON_KEY_JSON_BODY);
        return perform(request, jsonBody);
    }
}
