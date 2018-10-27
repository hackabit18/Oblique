package com.example.alihasan.androidhack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    NumberPicker np;
    EditText Phone1;
    Button next;
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        np = findViewById(R.id.np);
        Phone1 = findViewById(R.id.Phone1);
        next = findViewById(R.id.next);
        test = findViewById(R.id.testView);

        np.setMinValue(0);
        np.setMaxValue(1);

        np.setWrapSelectorWheel(true);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                switch(newVal)
                {
                    case 1:
                        Phone1.setVisibility(View.VISIBLE);
                    default:
                        Toast.makeText(MainActivity.this, "Only "+newVal+" Participant", Toast.LENGTH_SHORT).show();

                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SelectTime.class);
                startActivity(i);

                /**
                 * ADD NUMBER
                 * IN SHARED PREFERENCE
                 */

                SharedPrefClass storeMobile = new SharedPrefClass(MainActivity.this);
                storeMobile.saveMobile("mobile1 : ", Phone1.getText().toString());

                test.setText(new SharedPrefClass(MainActivity.this).getMobile("mobile1 : "));
            }
        });

    }
    }
