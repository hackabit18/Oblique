package com.example.alihasan.androidhack;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefClass {

    Context context;
    String mobile;
    String timings;
    int gearActivityState;

    public SharedPrefClass(Context context1){

        context = context1;
//        mobile = mobile1;

    }

    public void saveMobile(String code, String mobile1){

        mobile = mobile1;

        SharedPreferences sharedPref = context.getSharedPreferences("medical", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(code, mobile);
        editor.commit();
    }

    public String getMobile(String code){

        SharedPreferences sharedPref = context.getSharedPreferences("medical", Context.MODE_PRIVATE);
        String MobileValue = sharedPref.getString(code, "");

        return  MobileValue;
    }


    public void saveTimings(String code, String timings1){

        timings = timings1;
        SharedPreferences sharedPref = context.getSharedPreferences("medical", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(code, timings);
        editor.commit();
    }

    public String getTimings(String code){

        SharedPreferences sharedPref = context.getSharedPreferences("medical", Context.MODE_PRIVATE);
        String timingValue = sharedPref.getString(code, "-1");

        return  timingValue;
    }

    public void saveGearActivityState(String code, int gearActivityState1){

        gearActivityState = gearActivityState1;
        SharedPreferences sharedPref = context.getSharedPreferences("medical", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(code, gearActivityState);
        editor.commit();
    }

    public int getGearActivityState(String code){

        SharedPreferences sharedPref = context.getSharedPreferences("medical", Context.MODE_PRIVATE);
        int gearActivityValue = sharedPref.getInt(code, 0);

        return  gearActivityValue;
    }



}
