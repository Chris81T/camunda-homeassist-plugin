package de.ckthomas.smarthome.camunda.connectors.homeassistant.common;

import okhttp3.Response;
import org.camunda.connect.impl.AbstractConnectorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Christian Thomas
 */
public class CommonResponse extends AbstractConnectorResponse {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonResponse.class);

    private final Response response;

    public CommonResponse() {
        this(null);
    }

    public CommonResponse(Response response) {
        this.response = response;
    }

    @Override
    protected void collectResponseParameters(Map<String, Object> responseParameters) {

    }
}
