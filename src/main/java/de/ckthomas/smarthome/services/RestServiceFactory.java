package de.ckthomas.smarthome.services;

/**
 * This Factory provides a service client based on OkHttp3.
 * @author Christian Thomas
 */
public abstract class RestServiceFactory {

    private static RestService restService = null;

    public static boolean isInstantiated() {
        return restService != null;
    }

    public static boolean isNotInstantiated() {
        return !isInstantiated();
    }

    public static RestService getInstance() {
        return getInstance(null);
    }

    public static RestService getInstance(String basePath) {
        return getInstance(basePath, null, null);
    }

    public static RestService getInstance(String basePath, String authKey, String authValue) {
        if (isNotInstantiated()) {
            restService = new RestService(basePath, authKey, authValue);
        }
        return restService;
    }

}
