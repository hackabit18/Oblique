package com.example.alihasan.androidhack;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ReceiverService extends Service {

    final static private String TAG = "ALI";
    private Handler handler = new Handler();
    private Runnable runnable;
    MqttConnectOptions options = new MqttConnectOptions();



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand");


        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setUserName("aakashk_kvjp58");
        options.setPassword("46f406136c5e4e5f874e283f95ed3dfc".toCharArray());
        String clientId = MqttClient.generateClientId();
        final MqttAndroidClient client =
                new MqttAndroidClient(getApplicationContext(), "tcp://io.adafruit.com:1883",
                        clientId);

        runnable = new Runnable() {
            @Override
            public void run() {




                try {
                    IMqttToken token = client.connect(options);
                    token.setActionCallback(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            // We are connected
                            Log.d(TAG, "onSuccess");
//                    Toast toast = Toast.makeText(getApplicationContext(), "Connection Done", Toast.LENGTH_SHORT);
//                    toast.show();

                            //Subscribing
                            final String topic = "aakashk_kvjp58/f/switch1";
                            int qos = 1;
                            try {
                                IMqttToken subToken = client.subscribe(topic, qos);
                                subToken.setActionCallback(new IMqttActionListener() {
                                    @Override
                                    public void onSuccess(IMqttToken asyncActionToken) {

                                    }
                                    @Override
                                    public void onFailure(IMqttToken asyncActionToken,
                                                          Throwable exception) {
                                        // The subscription could not be performed, maybe the user was not
                                        // authorized to subscribe on the specified topic e.g. using wildcards
//                                Toast.makeText(MqttReceiver.this, "Couldn't subscribe to: " + topic, Toast.LENGTH_SHORT).show();


                                    }
                                });
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            // Something went wrong e.g. connection timeout or firewall problems
                            Log.d(TAG, "onFailure");
//                    Toast toast1 = Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT);
//                    toast1.show();


                        }
                    });


                } catch (MqttException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(runnable, 2000);

            }
        };
        handler.post(runnable);

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(getApplicationContext(), "LOST", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {



                Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "received");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

                Toast.makeText(getApplicationContext(), "completed", Toast.LENGTH_SHORT).show();
            }
        });


        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    @Override
//    public void onDestroy() {
//        Log.d(TAG, "inDestroy");
//        Intent i = new Intent(this, ReceiverBroadcast.class);
//        sendBroadcast(i);
//
//    }


}
