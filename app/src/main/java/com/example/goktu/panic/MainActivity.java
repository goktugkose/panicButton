package com.example.goktu.panic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    SharedPreferences preferences;
    String name, surname, phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        name = preferences.getString("name","N/A");
        surname = preferences.getString("surname", "N/A");
        phone = preferences.getString("phone", "N/A");


        Log.e("Name", name);
        Log.e("Surname", surname);
        Log.e("Phone Number", phone);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!name.equals("N/A")  && !surname.equals("N/A")  && !phone.equals("N/A") ){

                    Intent i = new Intent(MainActivity.this,Main4Activity.class);
                    startActivity(i);
                    finish();
                }

                else{
                    Intent homeIntent = new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(homeIntent);
                    finish();
                }



            }
        },SPLASH_TIME_OUT);

    }



}
