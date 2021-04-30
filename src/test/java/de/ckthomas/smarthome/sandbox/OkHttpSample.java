package de.ckthomas.smarthome.sandbox;

import okhttp3.*;

import java.io.IOException;

public class OkHttpSample {

    private static String AUTH_KEY = "Authorization";
    private static String AUTH_VAL = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIxNzNhNzI5NTNhYjk0NzY2OGZjZjgwNzFjZWEyNWE4OCIsImlhdCI6MTYwMzQ1MjY4NywiZXhwIjoxOTE4ODEyNjg3fQ.vR11aGMBWf6Bw07myHIy6m6Jr8XRyNpIgd-4bhDEm94";

    private static void getGoogleDe() throws IOException {

        final MediaType JSON = MediaType.get("application/json; charset=utf-8");

        OkHttpClient httpClient = new OkHttpClient();

        Request httpRequest = new Request.Builder()
                .url("https://www.google.de")
                .get()
                .build();

        Call call = httpClient.newCall(httpRequest);
        Response response = call.execute();

        String result = response.body().string();

        System.out.println("Response:\n" + result);
    }

    private static void rollershutterUp() throws IOException {
        System.out.println("Rolladen hoch....");
    }

    private static void rollershutterDown() throws IOException {
        System.out.println("Rolladen runter....");
    }

    /**
     *
     * @param value "on" or "off"
     * @throws IOException
     */
    private static void ctrlLight(String value) throws IOException {
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");

        OkHttpClient httpClient = new OkHttpClient();

        RequestBody body = RequestBody.create("{     \n" +
                "   \"entity_id\": \"switch.wohnzimmer_hue_tv_fenster_channel_2\" \n" +
                "} ", JSON);

        Request httpRequest = new Request.Builder()
                .url("http://jarvis.fritz.box:8123/api/services/switch/turn_" + value)
                .addHeader(AUTH_KEY, AUTH_VAL)
                .post(body)
                .build();

        Call call = httpClient.newCall(httpRequest);
        Response response = call.execute();

        /*
Fehler 404   --> es wird keine IOException geworfen
isSuccessful = false
message = Not Found
code = 404
sentRequestAtMillis = 1619727774897
receivedResponseAtMillis = 1619727774905

AKTION, welche keine Wirkung hat (Licht an, wenn LIcht schon an ist)
Licht an....
isSuccessful = true
message = OK
code = 200
sentRequestAtMillis = 1619727914802
receivedResponseAtMillis = 1619727914849
Response:
[]

AKTION, welche eine Wirkung hat
isSuccessful = true
message = OK
code = 200
sentRequestAtMillis = 1619727917910
receivedResponseAtMillis = 1619727917968
Response:
[{"entity_id": "switch.wohnzimmer_hue_tv_fenster_channel_2", "state": "off", "attributes": {"friendly_name": "Wohnzimmer Hue (Fenster 1 RGB)"}, "last_changed": "2021-04-29T20:25:08.339043+00:00", "last_updated": "2021-04-29T20:25:08.339043+00:00", "context": {"id": "c024ff65221e9fa661cdb0bfb143edeb", "parent_id": null, "user_id": "3e9690418396464b99f3b66e95bfa2e8"}}]

         */

        System.out.println("isSuccessful = " + response.isSuccessful());
        System.out.println("message = " + response.message());
        System.out.println("code = " + response.code());
        System.out.println("sentRequestAtMillis = " + response.sentRequestAtMillis());
        System.out.println("receivedResponseAtMillis = " + response.receivedResponseAtMillis());

        String result = response.body().string();
        System.out.println("Response:\n" + result);
    }

    private static void lightOn() throws IOException {
        System.out.println("Licht an....");
        ctrlLight("on");
    }

    private static void lightOff() throws IOException {
        System.out.println("Licht aus....");
        ctrlLight("off");
    }

    public static void main(String... args) throws Exception {
        System.out.println("This example will show how to use OkHttp...");
        //getGoogleDe();
        System.out.println("3...");
        Thread.sleep(500);
        System.out.println("2..");
        Thread.sleep(500);
        System.out.println("1.");
        Thread.sleep(500);
        try {
            lightOn();
        } catch (IOException e) {
            System.err.println("Ooooh no... " + e.getMessage());
        }
        Thread.sleep(1500);
        System.out.println("3...");
        Thread.sleep(500);
        System.out.println("2..");
        Thread.sleep(500);
        System.out.println("1.");
        Thread.sleep(500);
        lightOff();
    }

}
