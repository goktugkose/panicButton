package com.example.goktu.panic;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main5Activity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    String name, surname, phone;
    EditText et1,et2,et3;
    Button b1,b2;

    View v;

    ProgressDialog pd;

    int REQUEST_SELECT_PHONE_NUMBER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        name = preferences.getString("name","N/A");
        surname =  preferences.getString("surname", "N/A");
        phone = preferences.getString("phone","N/A");

        et1 = (EditText)findViewById(R.id.editText);
        et2 = (EditText)findViewById(R.id.editText2);
        et3 = (EditText)findViewById(R.id.editText3);

        et1.setText(name);
        et2.setText(surname);
        et3.setText(phone);

        b1 = (Button)findViewById(R.id.button);
        b2 = (Button)findViewById(R.id.button_2);

        String acil_name = preferences.getString("acil_name", "Kişi Ekleyin");
        String acil_name_2 = preferences.getString("acil_name_2", "Kişi Ekleyin");

        b1.setText(acil_name);
        b2.setText(acil_name_2);

    }

    public void logOut(View view) {

        new AsyncTask<String,String,String>(){

            @Override
            protected String doInBackground(String... params) {

                try {
                    Thread.sleep(2000);

                    editor.remove("name");
                    editor.remove("surname");
                    editor.remove("phone");
                    editor.remove("acil_name");
                    editor.remove("acil_phone");
                    editor.remove("acil_name_2");
                    editor.remove("acil_phone_2");
                    editor.commit();

                    Intent i = new Intent(Main5Activity.this, Main2Activity.class);
                    startActivity(i);
                    Main4Activity.getInstances().finish();
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(Main5Activity.this);
                pd.setMessage("Çıkış yapılıyor...");
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

    public void getContact_1(View view) {

        v = view;

        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);

        if(i.resolveActivity(getPackageManager())!= null){

            startActivityForResult(i,REQUEST_SELECT_PHONE_NUMBER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data != null){
            Uri contactUri = data.getData();
            String[] projection = new String [] {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

            Cursor cursor = getContentResolver().query(contactUri,projection,null,null,null);

            if(cursor != null && cursor.moveToFirst()){

                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(numberIndex);

                int numberIndex_2 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String phone = cursor.getString(numberIndex_2);


                if(v.getId() == b1.getId()){
                    b1.setText(name);
                    editor.putString("acil_name", name);
                    editor.putString("acil_phone", phone);
                    editor.commit();
                    Log.e("Get Phone #1 from Act_5", preferences.getString("acil_phone","N/A"));
                }

                else{
                    b2.setText(name);
                    editor.putString("acil_name_2", name);
                    editor.putString("acil_phone_2", phone);
                    editor.commit();
                    Log.e("Get Phone #2 from Act_5", preferences.getString("acil_phone_2","N/A"));
                }

            }
        }


    }

}
