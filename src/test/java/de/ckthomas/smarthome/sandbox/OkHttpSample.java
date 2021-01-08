package de.ckthomas.smarthome.sandbox;

import okhttp3.*;

import java.io.IOException;

public class OkHttpSample {

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
        System.out.println("ROlladen hoch....");
    }

    private static void rollershutterDown() throws IOException {
        System.out.println("ROlladen runter....");
    }

    public static void main(String... args) throws IOException {
        System.out.println("This example will show how to use OkHttp...");
        getGoogleDe();
    }

}
