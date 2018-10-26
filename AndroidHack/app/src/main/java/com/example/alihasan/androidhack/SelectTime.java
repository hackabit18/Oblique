package com.example.alihasan.androidhack;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

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

                int n = np.getValue();

//                TESTING INTENT

                Intent i = new Intent(SelectTime.this,DateActivity.class);
                i.putExtra("Value",n);
                startActivity(i);


                switch(n)
                {
                    case 1:
                        Toast.makeText(SelectTime.this,ct1.getText().toString(),Toast.LENGTH_LONG).show();
                        i.putExtra("ChooseTime1",ct1.getText().toString());
                        break;
                    case 2:
                        i.putExtra("ChooseTime1",ct1.getText().toString());
                        i.putExtra("ChooseTime2",ct2.getText().toString());
                        break;
                    case 3:
                        i.putExtra("ChooseTime1",ct1.getText().toString());
                        i.putExtra("ChooseTime2",ct2.getText().toString());
                        i.putExtra("ChooseTime3",ct3.getText().toString());
                        break;
                    case 4:
                        i.putExtra("ChooseTime1",ct1.getText().toString());
                        i.putExtra("ChooseTime2",ct2.getText().toString());
                        i.putExtra("ChooseTime3",ct3.getText().toString());
                        i.putExtra("ChooseTime4",ct4.getText().toString());
                        break;
                    default:
                        Toast.makeText(SelectTime.this,"0 selected",Toast.LENGTH_LONG).show();
                }



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
                chooseTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
            }
        }, currentHour, currentMinute, false);

        timePickerDialog.show();

    }

    }

