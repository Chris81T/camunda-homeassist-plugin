package de.ckthomas.smarthome.camunda.connectors.homeassistant.common;

import com.google.gson.Gson;
import de.ckthomas.smarthome.camunda.PluginConsts;
import de.ckthomas.smarthome.exceptions.HassioException;
import de.ckthomas.smarthome.services.RestServiceClient;
import de.ckthomas.smarthome.services.RestServiceClientFactory;
import okhttp3.Response;
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

    protected static final Logger LOGGER = LoggerFactory.getLogger(CommonConnector.class);

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
    protected String toJson(Map<String, Object> map) {
        return gson.toJson(map);
    }

    /**
     * basePath provides as value: "location:port/api/"
     *
     * https://developers.home-assistant.io/docs/api/rest/
     * provides for the services part following url structure: "/api/services/<domain>/<service>"
     *
     * Notice: The basePath is already set in the service instance
     *
     * @return
     */
    protected String createUrl(String path, String domain, String service) {
        return new StringBuilder()
                .append(path)
                .append("/")
                .append(domain)
                .append("/")
                .append(service)
                .toString();
    }

    protected String createServiceUrl(String domain, String service) {
        return createUrl(PluginConsts.Common.PATH_SERVICES, domain, service);
    }

    /**
     * Helper method. Handle basic var's like path; auth credentials only, of to that moment no service instance
     * exists.
     * After that, simply the existing service instance will be returned regardless some different values from
     * different processes.
     */
    private RestServiceClient getRestServiceClient(Map<String, Object> requestParameters) {
        if (RestServiceClientFactory.isNotInstantiated()) {
            final String basePath = checkParam(this.basePath, PluginConsts.Common.BASE_PATH, requestParameters);
            final String authKey = checkParam(this.authKey, PluginConsts.Common.AUTH_KEY, requestParameters);
            final String authValue = checkParam(this.authValue, PluginConsts.Common.AUTH_VAL, requestParameters);
            return RestServiceClientFactory.getInstance(basePath, authKey, authValue);
        } else {
            return RestServiceClientFactory.getInstance();
        }
    }

    protected ConnectorResponse perform(CommonRequest request, String url, String jsonBody) {
        try {
            Map<String, Object> requestParameters = request.getRequestParameters();
            LOGGER.info("Executing operation. Given common-request = {}, given request parameters = {}, given url = {}, " +
                            "given json-body = {}",
                    request,
                    requestParameters,
                    url,
                    jsonBody);

            RestServiceClient serviceClient = getRestServiceClient(requestParameters);
            Response serviceResponse = serviceClient.execute(url, jsonBody);

            if (serviceResponse.isSuccessful()) {
                CommonResponse response = new CommonResponse(serviceResponse);
                LOGGER.info("Service call is executed. Response = {}", response);
                return response;
            } else {
                String failure = serviceResponse.body().string();
                LOGGER.error("Http Rest Request Execution failed! Message = {}", failure);
                throw new HassioException(failure);
            }
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
        LOGGER.info("About to execute CommonConnector with given request parameters = {}", requestParameters);

        final String jsonBody = (String) requestParameters.get(PluginConsts.Common.KEY_JSON_BODY);
        final String path = (String) requestParameters.get(PluginConsts.Common.KEY_URL_PATH);
        final String domain = (String) requestParameters.get(PluginConsts.Common.KEY_URL_DOMAIN);
        final String service = (String) requestParameters.get(PluginConsts.Common.KEY_URL_SERVICE);

        return perform(request, createUrl(path, domain, service), jsonBody);
    }
}
