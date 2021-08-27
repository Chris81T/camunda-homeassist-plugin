package de.ckthomas.smarthome.camunda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Thomas
 */
public abstract class PluginConsts {
    public abstract class ConnectorIds {
        public static final String ID_COMMON = "hassio-common";
        public static final String ID_SWITCH = "hassio-switch";
        public static final String ID_COVER = "hassio-cover";
        public static final String ID_LIGHT = "hassio-light";
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

    public abstract class Light {
        public static final String DOMAIN = "light";
        public static final String JSON_BODY_ENTITY_ID = "entity_id";
    }

    public abstract class Cover {
        public static final String DOMAIN = "cover";
        public static final String JSON_BODY_ENTITY_ID = "entity_id";
    }

    public abstract class EnginePlugin {
        public static final String MQTT_TOPIC_SEPERATOR = ",";

        public static final String MQTT_SERVER_URI = "mqttServerURI";
        public static final String MQTT_USERNAME = "mqttUser";
        public static final String MQTT_PASSWORD = "mqttPassword";

        public static final String MQTT_TO_BPMN_SIGNAL_TOPIC = "mqttToBpmnSignalTopic";

        public static final String MQTT_PROCESS_START_TOPIC = "mqttProcessStartTopic";
        public static final String MQTT_PROCESS_START_TOPIC_DEFAULT = "camundahassio/processstart";
    }

    public abstract class EngineListener {
        public static final String ELEM_SIGNAL_REF = "signalRef";
        public static final String ELEM_SIGNAL_NAME = "name";
        public static final String EXT_PROP_RESULT_VAR_NAME = "resultVariable";
    }
}
