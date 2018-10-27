package com.example.alihasan.androidhack;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class SelectTime extends AppCompatActivity {

    EditText ct1;
    EditText ct2;
    EditText ct3;
    EditText ct4;

    //TIMER THINGS
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;

    NumberPicker np;

    Button next;

    /**
     * MQTT
     */
    MqttConnectOptions options;

    final static private String TAG = "ALI";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        np = findViewById(R.id.np);
        next = findViewById(R.id.next);

        //EDITTEXTS
        ct1 = findViewById(R.id.ct1);
        ct2 = findViewById(R.id.ct2);
        ct3 = findViewById(R.id.ct3);
        ct4 = findViewById(R.id.ct4);

        /**
         * MQTT
         */
        options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setUserName("aakashk_kvjp58");
        options.setPassword("46f406136c5e4e5f874e283f95ed3dfc".toCharArray());


        /**
         * *****************************************************************************************
         * CLICK LISTENERS
         * **/

        //Choose Timer 1
        ct1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(ct1);
            }
        });

        //Choose Timer 2
        ct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(ct2);

            }
        });

        //Choose Timer 3
        ct3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(ct3);

            }
        });

        //Choose Timer 4
        ct4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(ct4);

            }
        });

        /**
         * *****************************************************************************************
         * CLICK LISTENERS ENDS HERE
         * **/


        /**
         * *****************************************************************************************
         * NUMBER PICKER STARTS HERE
         * **/
        //Populate NumberPicker values from minimum and maximum value range
        //Set the minimum value of NumberPicker
        np.setMinValue(0);
        //Specify the maximum value/number of NumberPicker
        np.setMaxValue(4);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        np.setWrapSelectorWheel(true);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                switch (newVal)
                {
                    case 1:
                        ct1.setVisibility(View.VISIBLE);
                        ct2.setVisibility(View.GONE);
                        ct3.setVisibility(View.GONE);
                        ct4.setVisibility(View.GONE);
                        break;
                    case 2:
                        ct1.setVisibility(View.VISIBLE);
                        ct2.setVisibility(View.VISIBLE);
                        ct3.setVisibility(View.GONE);
                        ct4.setVisibility(View.GONE);
                        break;
                    case 3:
                        ct1.setVisibility(View.VISIBLE);
                        ct2.setVisibility(View.VISIBLE);
                        ct3.setVisibility(View.VISIBLE);
                        ct4.setVisibility(View.GONE);
                        break;
                    case 4:
                        ct1.setVisibility(View.VISIBLE);
                        ct2.setVisibility(View.VISIBLE);
                        ct3.setVisibility(View.VISIBLE);
                        ct4.setVisibility(View.VISIBLE);
                        break;
                    default:
                        ct1.setVisibility(View.GONE);
                        ct2.setVisibility(View.GONE);
                        ct3.setVisibility(View.GONE);
                        ct4.setVisibility(View.GONE);
                        Toast.makeText(SelectTime.this,"1 2 3 4 :)!!!",Toast.LENGTH_LONG).show();
                }
            }
        });

        /**
         * *****************************************************************************************
         * NUMBER PICKER ENDS HERE
         * **/

        /**
         * *****************************************************************************************
         * NEXT BUTTON
         * **/

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * MQTT
                 */
                options = new MqttConnectOptions();
                options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
                options.setUserName("aakashk_kvjp58");
                options.setPassword("46f406136c5e4e5f874e283f95ed3dfc".toCharArray());

//                int n = np.getValue();

//                TESTING INTENT
//
//                Intent i = new Intent(SelectTime.this,DateActivity.class);
//                i.putExtra("Value",n);
//                startActivity(i);


//                switch(n)
//                {
//                    case 1:
//                        Toast.makeText(SelectTime.this,ct1.getText().toString(),Toast.LENGTH_LONG).show();
//                        sendMessagetoAakash(removeLastTwo(ct1.getText().toString()));
//
////                        i.putExtra("ChooseTime1",ct1.getText().toString());
//                        break;
//                    case 2:
//                        sendMessagetoAakash(removeLastTwo(ct1.getText().toString()));
//                        sendMessagetoAakash(removeLastTwo(ct2.getText().toString()));
//
////                        i.putExtra("ChooseTime1",ct1.getText().toString());
////                        i.putExtra("ChooseTime2",ct2.getText().toString());
//                        break;
//                    case 3:
//                        sendMessagetoAakash(removeLastTwo(ct1.getText().toString()));
//                        sendMessagetoAakash(removeLastTwo(ct2.getText().toString()));
//                        sendMessagetoAakash(removeLastTwo(ct3.getText().toString()));
//
////                        i.putExtra("ChooseTime1",ct1.getText().toString());
////                        i.putExtra("ChooseTime2",ct2.getText().toString());
////                        i.putExtra("ChooseTime3",ct3.getText().toString());
//                        break;
//                    case 4:
//                        sendMessagetoAakash(removeLastTwo(ct1.getText().toString()));
//                        sendMessagetoAakash(removeLastTwo(ct2.getText().toString()));
//                        sendMessagetoAakash(removeLastTwo(ct3.getText().toString()));
//                        sendMessagetoAakash(removeLastTwo(ct4.getText().toString()));
//
////                        i.putExtra("ChooseTime1",ct1.getText().toString());
////                        i.putExtra("ChooseTime2",ct2.getText().toString());
////                        i.putExtra("ChooseTime3",ct3.getText().toString());
////                        i.putExtra("ChooseTime4",ct4.getText().toString());
//                        break;
//                    default:
//                        Toast.makeText(SelectTime.this,"0 selected",Toast.LENGTH_LONG).show();
//                }

                new SharedPrefClass(SelectTime.this).saveTimings("timings1 : ", ct1.getText().toString());
                new SharedPrefClass(SelectTime.this).saveTimings("timings2 : ", ct2.getText().toString());
                new SharedPrefClass(SelectTime.this).saveTimings("timings3 : ", ct3.getText().toString());
                new SharedPrefClass(SelectTime.this).saveTimings("timings4 : ", ct4.getText().toString());

                /**
                 * Sending JSON to MQTT
                 */

                String jsonMqtt = "{\n" +
                        "  \"value\":\n" +
                        "\t\t{\n" +
                        "\t\t\t\"t1\":["+commaString(new SharedPrefClass(SelectTime.this).getTimings("timings1 : "))+"],  \n" +
                        "\t\t\t\"t2\":["+commaString(new SharedPrefClass(SelectTime.this).getTimings("timings2 : "))+"],\n" +
                        "\t\t\t\"t3\":["+commaString(new SharedPrefClass(SelectTime.this).getTimings("timings3 : "))+"]\n" +
                        "\t\t}\n" +
                        "}\n";

                sendMessagetoAakash(jsonMqtt);

            }
        });

        /**
         * *****************************************************************************************
         * NEXT BUTTON ENDS HERE
         * **/


    }

    public void setTime(final EditText chooseTime){
        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(SelectTime.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                if (hourOfDay >= 12) {
                    amPm = "PM";
                } else {
                    amPm = "AM";
                }
                chooseTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
            }
        }, currentHour, currentMinute, false);

        timePickerDialog.show();

    }

    public String commaString(String str){

        StringBuilder cStr = new StringBuilder(str);
//        cStr.setCharAt(2,',');

        if(str.equals("")) {
            return "-1";
        }
        else
            cStr.setCharAt(2,',');
        return cStr.toString();
    }

    private void sendMessagetoAakash(final String str){

        String clientId = MqttClient.generateClientId();
        final MqttAndroidClient client =
                new MqttAndroidClient(getApplicationContext(), "tcp://io.adafruit.com:1883",
                        clientId);

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    Toast toast = Toast.makeText(getApplicationContext(), "Connection Done", Toast.LENGTH_SHORT);
                    toast.show();

                    //Subscribing
                    final String topic = "aakashk_kvjp58/f";
                    int qos = 1;
                    try {
                        IMqttToken subToken = client.subscribe(topic, qos);
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                // The message was published
                                Toast.makeText(SelectTime.this, "Successfully subscribed to: " + topic, Toast.LENGTH_SHORT).show();

                                //sending the message

                                String user_message = str;
                                String topic = "aakashk_kvjp58/f/t1";
                                String payload = user_message;
                                byte[] encodedPayload = new byte[0];
                                try {
                                    encodedPayload = payload.getBytes("UTF-8");
                                    MqttMessage message = new MqttMessage(encodedPayload);
                                    client.publish(topic, message);
                                    Toast.makeText(SelectTime.this, "Message Sent", Toast.LENGTH_SHORT).show();

                                } catch (UnsupportedEncodingException | MqttException e) {
                                    e.printStackTrace();
                                }

                                // disconnecting after task is done
                                try {
                                    IMqttToken disconToken = client.disconnect();
                                    disconToken.setActionCallback(new IMqttActionListener() {
                                        @Override
                                        public void onSuccess(IMqttToken asyncActionToken) {
                                            // we are now successfully disconnected
                                        }

                                        @Override
                                        public void onFailure(IMqttToken asyncActionToken,
                                                              Throwable exception) {
                                            // something went wrong, but probably we are disconnected anyway
                                        }
                                    });
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                // The subscription could not be performed, maybe the user was not
                                // authorized to subscribe on the specified topic e.g. using wildcards
                                Toast.makeText(SelectTime.this, "Couldn't subscribe to: " + topic, Toast.LENGTH_SHORT).show();


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
                    Toast toast1 = Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT);
                    toast1.show();


                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private String removeLastTwo(String s){
        String str = s.substring(0, s.length() - 2);
        return str;
    }

}





