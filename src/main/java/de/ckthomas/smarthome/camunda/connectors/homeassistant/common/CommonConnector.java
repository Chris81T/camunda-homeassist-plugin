package de.ckthomas.smarthome.camunda.connectors.homeassistant.common;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.camunda.connect.impl.AbstractConnector;
import org.camunda.connect.spi.ConnectorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class CommonConnector extends AbstractConnector<CommonRequest, CommonResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonConnector.class);

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient httpClient = new OkHttpClient();

    public CommonConnector(String connectorId) {
        super(connectorId);
    }

    @Override
    public CommonRequest createRequest() {
        LOGGER.info("Creating HassioConnector-Request");
        return new CommonRequest(this);
    }

    @Override
    public ConnectorResponse execute(CommonRequest request) {
        Map<String, Object> requestParameters = request.getRequestParameters();
        LOGGER.info("Executing operation. Given request = {}, given request parameters = {}", request,
                requestParameters);

        // https://square.github.io/okhttp/recipes/

        RequestBody body = RequestBody.create("{'some':'json'}", JSON);

        Request httpRequest = new Request.Builder()
                .url("some url")
                .post(body)
                .build();

        CommonResponse response = new CommonResponse();

        response.getResponseParameters().put("response", "Das soll einfach mal ein Ergebnis sein...");

        return response;
    }
}
