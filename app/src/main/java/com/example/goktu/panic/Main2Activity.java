package com.example.goktu.panic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    Switch sw;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sw = (Switch) findViewById(R.id.rb);


        if (checkPermission()) {
            Log.e("checkAllPermission", "All permissions granted.");


        } else {
            requestPermission();
        }
    }

    public void Devam(View view) {

        if (!sw.isChecked()) {
            Intent i = new Intent(Main2Activity.this, Main3Activity.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(Main2Activity.this, Main4Activity.class);
            startActivity(i);
            finish();
        }
    }

    public void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CODE);

    }

    public boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int result_2 = ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int result_3 = ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.READ_CONTACTS);


        if (result == PackageManager.PERMISSION_GRANTED &&
                result_2 == PackageManager.PERMISSION_GRANTED &&
                result_3 == PackageManager.PERMISSION_GRANTED) {

            Log.e("checkPermission - Act_2", "true");

            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(Main2Activity.this,
                            "İzinler verildi!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Main2Activity.this,
                            "İzinler reddedildi!", Toast.LENGTH_LONG).show();
                }

                break;
        }

    }

}


