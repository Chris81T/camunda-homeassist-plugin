package de.ckthomas.smarthome.camunda.connectors;

import de.ckthomas.smarthome.camunda.connectors.requests.HassioConnectorRequest;
import de.ckthomas.smarthome.camunda.connectors.responses.HassioConnectorResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.camunda.connect.impl.AbstractConnector;
import org.camunda.connect.spi.ConnectorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HassioConnector extends AbstractConnector<HassioConnectorRequest, HassioConnectorResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HassioConnector.class);

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient httpClient = new OkHttpClient();

    public HassioConnector(String connectorId) {
        super(connectorId);
    }

    @Override
    public HassioConnectorRequest createRequest() {
        LOGGER.info("Creating HassioConnector-Request");
        return new HassioConnectorRequest(this);
    }

    @Override
    public ConnectorResponse execute(HassioConnectorRequest request) {
        Map<String, Object> requestParameters = request.getRequestParameters();
        LOGGER.info("Executing operation. Given request = {}, given request parameters = {}", request,
                requestParameters);

        // https://square.github.io/okhttp/recipes/

        RequestBody body = RequestBody.create("{'some':'json'}", JSON);

        Request httpRequest = new Request.Builder()
                .url("some url")
                .post(body)
                .build();

        HassioConnectorResponse response = new HassioConnectorResponse();

        response.getResponseParameters().put("respone", "Das soll einfach mal ein Ergebnis sein...");

        return response;
    }
}
