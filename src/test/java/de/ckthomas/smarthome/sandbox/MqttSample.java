package de.ckthomas.smarthome.sandbox;

import de.ckthomas.smarthome.services.MqttToSignalServiceFactory;
import de.ckthomas.smarthome.services.ProcessStarterService;
import de.ckthomas.smarthome.services.ProcessStarterServiceFactory;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MqttSample {

    private static void listonToMqttViaService(String password) throws MqttException, InterruptedException {
//        ProcessStarterService processStarterService = ProcessStarterServiceFactory.getInstance(
//                null,
//                "camundahassio/processstart",
//                "tcp://jarvis.fritz.box:1883",
//                "hassio",
//                password != null ? password.toCharArray() : null
//        );
//
//        processStarterService.start();
//        System.out.println("hold the instance - manually termination is required");

//        MqttToSignalServiceFactory.setConnectionDetailsGlobally(
//                "tcp://jarvis.fritz.box:1883",
//                "hassio",
//                password != null ? password.toCharArray() : null
//        );
//
//        MqttToSignalServiceFactory.constructRuntimeSubscription(
//                "homeassistant/weather/mcu_home/state",
//                "123",
//                "abc",
//                null,
//                Optional.of("resultVariable")
//        );

// ACHTUNG: Gegen den gleichen Broker geht leider nicht!
//        MqttToSignalServiceFactory.constructRuntimeSubscription(
//                "homeassistant/weather/mcu_home/temperature",
//                "123",
//                "xyz",
//                null,
//                Optional.of("resultVariable")
//        );

    }

    private static void listenToMqttSwitch(String password) throws MqttException, InterruptedException {
        final String publisherId = "Test_Case_Sample";
        IMqttClient mqttClient = new MqttClient("tcp://jarvis.fritz.box:1883", publisherId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setUserName("hassio");
        options.setPassword(password.toCharArray());

        mqttClient.connect(options);
        System.out.println("mqtt client is connected :-)");

        // Damit die Methode nicht direkt beendet wird:
        CountDownLatch latch = new CountDownLatch(5);

        mqttClient.subscribe("homeassistant/light/hue_rgb_rechte_seite/state", ((topic, message) -> {
            System.out.println("Received message from topic = " + topic + " with content = " + message.toString());

            // einmal runterzählen
            latch.countDown();
        }));

        System.out.println("await one minute...");
        latch.await(1, TimeUnit.MINUTES);

        System.out.println("about to disconnect...");
        mqttClient.disconnect();
    }

    public static void main(String[] args) throws Exception {
        listonToMqttViaService(args[0]);
        listenToMqttSwitch(args[0]);
        System.out.println("Finished...");
    }

}
