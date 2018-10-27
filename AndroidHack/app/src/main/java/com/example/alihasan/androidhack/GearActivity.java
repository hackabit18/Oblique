package com.example.alihasan.androidhack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GearActivity extends AppCompatActivity {

    TextView timeView;
    TextView phoneView;

    TextView tv1;
    TextView tv2;
    TextView tv3;

    ImageView gearIcon;

    Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gear);

//        tv1 = findViewById(R.id.tv1);
//        tv2 = findViewById(R.id.tv2);
//        tv3 = findViewById(R.id.tv3);

        timeView = findViewById(R.id.timeView);
        phoneView = findViewById(R.id.phoneView);


        gearIcon = findViewById(R.id.gearIcon);

        reset = findViewById(R.id.reset);

        int n = new SharedPrefClass(GearActivity.this).getGearActivityState("gear");

        if(n == 1){
            phoneView.setVisibility(View.VISIBLE);
            timeView.setVisibility(View.VISIBLE);
            gearIcon.setVisibility(View.GONE);
            reset.setVisibility(View.VISIBLE);

            //PARTICIPANTS
            String s1 = new SharedPrefClass(GearActivity.this).getMobile("mobile1");
            String s2 = new SharedPrefClass(GearActivity.this).getMobile("mobile2");
            String s3 = new SharedPrefClass(GearActivity.this).getMobile("mobile3");

            phoneView.setText(s1+"\n"+
                    s2+"\n"+
                    s3);

//            int np = new SharedPrefClass(GearActivity.this).getNumber("partNumber");
//
//            switch(np){
//                case 3:
//                    tv3.setVisibility(View.VISIBLE);
//                    tv3.setText(s3);
//                case 2:
//                    tv2.setVisibility(View.VISIBLE);
//                    tv2.setText(s2);
//                case 1:
//                    tv1.setVisibility(View.VISIBLE);
//                    tv1.setText(s1);
//            }



            //TIME
            String str1 = new SharedPrefClass(GearActivity.this).getTimings("timings1");
            String str2 = new SharedPrefClass(GearActivity.this).getTimings("timings2");
            String str3 = new SharedPrefClass(GearActivity.this).getTimings("timings3");

            timeView.setText(str1+"\n"+
                              str2+"\n"+
                               str3);

        }
        else{
//            tv1.setVisibility(View.GONE);
//            tv2.setVisibility(View.GONE);
//            tv3.setVisibility(View.GONE);
            phoneView.setVisibility(View.GONE);
            timeView.setVisibility(View.GONE);
            reset.setVisibility(View.GONE);
        }

        gearIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GearActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }


}
