package com.example.alihasan.androidhack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class GearActivity extends AppCompatActivity {

    TextView timeView;
    TextView phoneView;

    ImageView gearIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gear);

        timeView = findViewById(R.id.timeView);
        phoneView = findViewById(R.id.phoneView);

        gearIcon = findViewById(R.id.gearIcon);


    }


}
