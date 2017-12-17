package com.example.goktu.panic;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main4Activity extends AppCompatActivity {

    TextView tw;
    Button b;
    Switch sw;
    String tel, tel_2, name, surname, a;
    String address = "Konum bulunamıyor!";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    Location location;
    Geocoder geocoder;
    List<Address> addresses;
    double longitude = 0;
    double latitude = 0;

    ProgressDialog pd;

    boolean check = false;

    static Main4Activity main4Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        main4Activity = this;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        b = (Button) findViewById(R.id.btn);
        tw = (TextView) findViewById(R.id.tw_name);
        sw = (Switch) findViewById(R.id.sw);

        name = preferences.getString("name", "N/A");
        surname = preferences.getString("surname", "N/A");

        tel = preferences.getString("acil_phone", "N/A");
        tel_2 = preferences.getString("acil_phone_2", "N/A");

    }

    @Override
    protected void onResume() {
        super.onResume();
        setText(null);
        }

    public void doOp(View view) {

        Log.e("Get Phone #1 from Act_4", tel);
        Log.e("Get Phone #2 from Act_4", tel_2);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(Main4Activity.this, "Lütfen konum servislerini etkinleştirin!", Toast.LENGTH_SHORT).show();
        } else if (tel.equals("N/A")  && tel_2.equals("N/A")) {
            Toast.makeText(Main4Activity.this, "Lütfen ayarlar sekmesinden acil durumda aranacak en az bir kişi ekleyiniz.", Toast.LENGTH_LONG).show();
        } else {
            buttonAnimation();

            new AsyncTask<String, String, String>() {

                @Override
                protected String doInBackground(String... params) {
                    try {
                        Thread.sleep(2000);
                        Message();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pd = new ProgressDialog(Main4Activity.this);
                    pd.setMessage("Mesaj oluşturuluyor...");
                    pd.show();
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    pd.dismiss();
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                    pd.dismiss();
                }
            }.execute();
        }
    }

    public void switchGPS(View view) {

        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(i);

    }

    public boolean checkGPS() throws Settings.SettingNotFoundException  {


        if (Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE) == 3) {
            Log.e("checkGPS - Act4", Integer.toString(Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE)));
            sw.setChecked(true);
            return true;
        }

        else {
            Log.e("checkGPS - Act4", Integer.toString(Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE)));
            Toast.makeText(Main4Activity.this, "Lütfen yüksek doğruluk GPS modunu kullanınız.", Toast.LENGTH_SHORT).show();
            sw.setChecked(false);
            return false;
        }
    }

    public void buttonAnimation() {

        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(1000);
        b.startAnimation(animation);
    }

    public void Message() {

        String message = name + " " + surname + " panik butonuna bastı!\n" +
                "\nAdres : " + address + "\n\nKoordinatlar : " + latitude + " , " + longitude;

        Uri u = Uri.parse("sms:" + tel + ";" + tel_2);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(u);
        intent.putExtra("sms_body", message);
        startActivity(intent);
    }

    public void getLocation() {
                        if (ActivityCompat.checkSelfPermission(Main4Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(Main4Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(Main4Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                        }
                        else{
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {

                                longitude = location.getLongitude();
                                latitude = location.getLatitude();

                                Log.e("Latitude", latitude + "");
                                Log.e("Longitude", longitude + "");

                            }

                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,2000,10,locationListener);
                            geocoder = new Geocoder(Main4Activity.this, Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (addresses.size() > 0 && addresses != null) {
                                address = addresses.get(0).getAddressLine(0);

                            }

                            Log.e("Address: " , address);

                        }
                }

    public void account(View view) {

        Intent i = new Intent(Main4Activity.this, Main5Activity.class);
        startActivity(i);

    }

    private void showAlert() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Konum Servislerini Etkinleştir")
                .setMessage("Konum servisleri devre dışı!\nUygulamayı kullanmak için " +
                        "lütfen konum servislerini etkinleştirin.")
                .setPositiveButton("KONUM SERVİSİ AYARLARI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                getLocation();
                break;
        }

    }

    public static Main4Activity getInstances(){

        return main4Activity;
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void setText(View v){

        tel = preferences.getString("acil_phone", "N/A");
        tel_2 = preferences.getString("acil_phone_2", "N/A");

        try {
            check = checkGPS();
        }
        catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (check) {
            getLocation();
            a = "Hoşgeldiniz, " + name + " " + surname + "!\n\nKonumunuz:\n" + address;
            tw.setText(a);
        }

        else {
            address = "Konum bulunamıyor!";
            a = "Hoşgeldiniz, " + name + " " + surname + "!\n\nKonumunuz:\n" + address;
            showAlert();
            tw.setText(a);
        }
    }
}









