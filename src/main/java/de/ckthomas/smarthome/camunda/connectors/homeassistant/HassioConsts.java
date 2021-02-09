package de.ckthomas.smarthome.camunda.connectors.homeassistant;

/**
 * @author Christian Thomas
 */
public abstract class HassioConsts {
    public abstract class ConnectorIds {
        public static final String ID_COMMON = "hassio-common";
        public static final String ID_SWITCH = "hassio-switch";
    }

    public abstract class Common {
        public static final String AUTH_KEY = "authKey";
        public static final String AUTH_VAL = "authValue";
        public static final String BASE_PATH = "basePath";
        public static final String PATH_SERVICES = "services";

        public static final String KEY_URL_PATH = "path";
        public static final String KEY_URL_DOMAIN = "domain";
        public static final String KEY_URL_SERVICE = "service";
        public static final String KEY_JSON_BODY = "jsonBody";
    }

    public abstract class Switch {
        public static final String DOMAIN = "switch";
        public static final String JSON_BODY_ENTITY_ID = "entity_id";
    }
}
