package de.ckthomas.smarthome.camunda.connectors.homeassistant.common;

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

    @Override
    public ConnectorResponse execute(CommonRequest request) {
        Map<String, Object> requestParameters = request.getRequestParameters();
        LOGGER.info("Executing operation. Given request = {}, given request parameters = {}", request,
                requestParameters);

        final String url = (String) requestParameters.get("url");
        final String jsonBody = (String) requestParameters.get("jsonBody");

        RestService service = RestServiceFactory.getInstance(basePath, authKey, authValue);

        try {
            service.execute(url, jsonBody);
        } catch (IOException e) {
            e.printStackTrace(); // Umgang hier?
        }

        CommonResponse response = new CommonResponse();
        return response;
    }
}
