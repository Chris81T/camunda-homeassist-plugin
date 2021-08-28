package de.ckthomas.smarthome.services;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MqttToSignalServiceJsonCheckupTest {

    protected final Gson gson = new Gson();

    private MqttToSignalService service = new MqttToSignalService(
            null,
            null,
            null,
            null,
            null,
            null
    );

    @Test
    public void checkRealJsonVal() {
        String val = "{\"val\":\"someVal\", \"date\":\"now\", \"nr\":50}";
        Object json = gson.fromJson(val, Object.class);
        System.out.println("JSON Instance = " + json + ", Class = " + json.getClass().getName());
        Assertions.assertTrue(AbstractMqttService.ValueTypes.JSON_ENTRY.equals(service.checkValueType(val)));
    }

    @Test
    public void checkJsonArray() {
        String val = "[{\"val1\":\"someVal1\"},{\"val2\":\"someVal2\"}]";
        Object json = gson.fromJson(val, Object.class);
        System.out.println("JSON Instance = " + json + ", Class = " + json.getClass().getName());
        Assertions.assertTrue(AbstractMqttService.ValueTypes.ARRAY.equals(service.checkValueType(val)));
    }

    @Test
    public void checkSimpleStringVal() {
        String val = "\"Today it is sunny\"";
        Object json = gson.fromJson(val, Object.class);
        System.out.println("JSON Instance = " + json + ", Class = " + json.getClass().getName());
        Assertions.assertTrue(AbstractMqttService.ValueTypes.PRIMITIVE_ENTRY.equals(service.checkValueType(val)));
    }

    @Test
    public void checkSimpleNonStringVal() {
        String val = "1234";
        Object json = gson.fromJson(val, Object.class);
        System.out.println("JSON Instance = " + json + ", Class = " + json.getClass().getName());
        Assertions.assertTrue(AbstractMqttService.ValueTypes.PRIMITIVE_ENTRY.equals(service.checkValueType(val)));
    }


    @Test
    public void checkSimpleValsAsArray() {
        String val = "[\"Today it is sunny\",\"Today it is cloudy\",\"Today it is rainy\"]";
        Object json = gson.fromJson(val, Object.class);
        System.out.println("JSON Instance = " + json + ", Class = " + json.getClass().getName());
        Assertions.assertTrue(AbstractMqttService.ValueTypes.ARRAY.equals(service.checkValueType(val)));
    }

}
