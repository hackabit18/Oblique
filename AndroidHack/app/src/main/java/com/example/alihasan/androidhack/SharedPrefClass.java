package com.example.alihasan.androidhack;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefClass {

    Context context;
    String mobile;

    public SharedPrefClass(Context context1){

        context = context1;
//        mobile = mobile1;

    }

    public void saveMobile(String code, String mobile1){

        mobile = mobile1;

        SharedPreferences sharedPref = context.getSharedPreferences("mobile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(code, mobile);
        editor.commit();
    }

    public String getMobile(String code){

        SharedPreferences sharedPref = context.getSharedPreferences("mobile", Context.MODE_PRIVATE);
        String timingValue = sharedPref.getString(code, "-1");

        return  timingValue;
    }




}