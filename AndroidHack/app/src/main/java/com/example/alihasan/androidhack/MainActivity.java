package com.example.alihasan.androidhack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    NumberPicker np;

    EditText Phone1;
    EditText Phone2;
    EditText Phone3;


    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        np = findViewById(R.id.np);

        Phone1 = findViewById(R.id.Phone1);
        Phone2 = findViewById(R.id.Phone2);
        Phone3 = findViewById(R.id.Phone3);

        next = findViewById(R.id.next);

        np.setMinValue(1);
        np.setMaxValue(3);

        np.setWrapSelectorWheel(true);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                switch(newVal)
                {
                    case 1:
                        Phone1.setVisibility(View.VISIBLE);
                        Phone2.setVisibility(View.GONE);
                        Phone3.setVisibility(View.GONE);
                        break;
                    case 2:
                        Phone1.setVisibility(View.VISIBLE);
                        Phone2.setVisibility(View.VISIBLE);
                        Phone3.setVisibility(View.GONE);
                        break;
                    case 3:
                        Phone3.setVisibility(View.VISIBLE);
                        Phone2.setVisibility(View.VISIBLE);
                        Phone3.setVisibility(View.VISIBLE);
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Only "+newVal+" Participants :) !!!", Toast.LENGTH_SHORT).show();

                }

//                new SharedPrefClass(MainActivity.this).saveNumber("partNumber", newVal);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /**
                 * ADD PARTICIPANTS NUMBER
                 * IN SHARED PREFERENCE
                 */

                SharedPrefClass storeMobile1 = new SharedPrefClass(MainActivity.this);
                storeMobile1.saveMobile("mobile1", Phone1.getText().toString());

                SharedPrefClass storeMobile2 = new SharedPrefClass(MainActivity.this);
                storeMobile2.saveMobile("mobile2", Phone2.getText().toString());

                SharedPrefClass storeMobile3 = new SharedPrefClass(MainActivity.this);
                storeMobile3.saveMobile("mobile3", Phone3.getText().toString());

                //GET PHONE FROM HERE
//                new SharedPrefClass(MainActivity.this).getMobile("mobile1 : ");

                Intent i = new Intent(MainActivity.this,SelectTime.class);
                startActivity(i);

            }
        });

    }
    }
