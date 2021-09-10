package de.ckthomas.smarthome.services;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This facade provides common logic for a simpler usage.
 * @author Christian Thomas
 */
public class RestServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestServiceClient.class);

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    /**
     * use only one instance of the http-client.
     */
    private OkHttpClient httpClient = new OkHttpClient();

    private final String basePath;
    private final String authKey;
    private final String authValue;

    /**
     * Use the factory to get an instance of this.
     */
    RestServiceClient(String basePath, String authKey, String authValue) {
        this.basePath = basePath;
        this.authKey = authKey;
        this.authValue = authValue;
    }

    public Request.Builder createRequestBuilder(String url) {
        final String prefix = basePath != null ? basePath + "/" : "";
        final String finalUrl = prefix + url;
        LOGGER.info("Using final url = {} for creating request builder", finalUrl);

        final Request.Builder builder = new Request.Builder()
                .url(finalUrl);
        if (authKey != null && authValue != null) {
            LOGGER.info("Adding header with authKey = {}, authValue = {}", authKey, "***secret***");
            builder.addHeader(authKey, authValue);
        }
        return builder;
    }

    public Request createPostRequest(String url, String jsonBody) {
        return createRequestBuilder(url)
            .post(RequestBody.create(jsonBody, JSON))
            .build();
    }

    public Call createCall(Request request) {
        return httpClient.newCall(request);
    }

    public Response execute(Request request) throws IOException {
        return createCall(request).execute();
    }

    public Response execute(String url, String jsonBody) throws IOException {
        return execute(createPostRequest(url, jsonBody));
    }

}
