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
        public static final String COMMON_AUTH_KEY = "authKey";
        public static final String COMMON_AUTH_VAL = "authValue";
        public static final String COMMON_BASE_PATH = "basePath";

        public static final String COMMON_KEY_URL = "url";
        public static final String COMMON_KEY_JSON_BODY = "jsonBody ";
    }

    public abstract class Switch {
        public static final String SWITCH_KEY_ENTITY_ID = "entity_id";
    }
}
