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

    /**
     * basePath provides as value: "location:port/api/"
     *
     * https://developers.home-assistant.io/docs/api/rest/
     * provides for the services part following url structure: "/api/services/<domain>/<service>"
     * @return
     */
    protected String createUrl(String path, String domain, String service) {
        return new StringBuilder()
                .append(basePath)
                .append(path)
                .append("/")
                .append(domain)
                .append("/")
                .append(service)
                .toString();
    }

    protected String createServiceUrl(String domain, String service) {
        return createUrl(HassioConsts.Common.PATH_SERVICES, domain, service);
    }

    protected ConnectorResponse perform(CommonRequest request, String url, String jsonBody) {
        try {
            Map<String, Object> requestParameters = request.getRequestParameters();
            LOGGER.info("Executing operation. Given request = {}, given request parameters = {}", request,
                    requestParameters);

            final String basePath = checkParam(this.basePath, HassioConsts.Common.BASE_PATH, requestParameters);
            final String authKey = checkParam(this.authKey, HassioConsts.Common.AUTH_KEY, requestParameters);
            final String authValue = checkParam(this.authValue, HassioConsts.Common.AUTH_VAL, requestParameters);

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
        final String jsonBody = (String) requestParameters.get(HassioConsts.Common.KEY_JSON_BODY);
        final String path = (String) requestParameters.get(HassioConsts.Common.KEY_URL_PATH);
        final String domain = (String) requestParameters.get(HassioConsts.Common.KEY_URL_DOMAIN);
        final String service = (String) requestParameters.get(HassioConsts.Common.KEY_URL_SERVICE);

        return perform(request, createUrl(path, domain, service), jsonBody);
    }
}
