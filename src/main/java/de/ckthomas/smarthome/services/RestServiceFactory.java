package de.ckthomas.smarthome.services;

/**
 * This Factory provides a service client based on OkHttp3.
 */
public abstract class RestServiceFactory {

    private static RestService restService = null;

    public static RestService getInstance() {
        return getInstance(null);
    }

    public static RestService getInstance(String basePath) {
        return getInstance(basePath, null, null);
    }

    public static RestService getInstance(String basePath, String authKey, String authValue) {
        if (restService == null) {
            restService = new RestService(basePath, authKey, authValue);
        }
        return restService;
    }

}
