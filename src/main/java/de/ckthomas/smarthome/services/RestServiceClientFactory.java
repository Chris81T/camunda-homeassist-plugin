package de.ckthomas.smarthome.services;

/**
 * This Factory provides a service client based on OkHttp3.
 * @author Christian Thomas
 */
public abstract class RestServiceClientFactory {

    private static RestServiceClient restServiceClient = null;

    public static boolean isInstantiated() {
        return restServiceClient != null;
    }

    public static boolean isNotInstantiated() {
        return !isInstantiated();
    }

    public static RestServiceClient getInstance() {
        return getInstance(null);
    }

    public static RestServiceClient getInstance(String basePath) {
        return getInstance(basePath, null, null);
    }

    public static RestServiceClient getInstance(String basePath, String authKey, String authValue) {
        if (isNotInstantiated()) {
            restServiceClient = new RestServiceClient(basePath, authKey, authValue);
        }
        return restServiceClient;
    }

}
