package de.ckthomas.smarthome.sandbox;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GsonSample {
    public static void main(String[] args) {
        Gson gson = new Gson();

        Map<String, Object> values = new HashMap<>();
        values.put("simpleKey", "STRING_VALUE");
        values.put("simpleNr", 45);
        values.put("simpleList", Arrays.asList(255,0,34));
        System.out.println(gson.toJson(values));
    }
}
