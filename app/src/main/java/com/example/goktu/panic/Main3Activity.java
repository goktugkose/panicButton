package com.example.goktu.panic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {

    EditText et1,et2,et3;
    String a,b,c;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        et1 = (EditText)findViewById(R.id.et_name);
        et2 = (EditText)findViewById(R.id.et_sname);
        et3 = (EditText)findViewById(R.id.et_phone);


        String ad = preferences.getString("name","");
        String soyad = preferences.getString("surname","");
        String numara = preferences.getString("phone","");

        et1.setText(ad);
        et2.setText(soyad);
        et3.setText(numara);

    }

    public void SignUp(View view) {

        a = et1.getText().toString();
        b = et2.getText().toString();
        c = et3.getText().toString();

        if(et1.getText().length() < 1 && et2.getText().length() < 1){

            Toast.makeText(Main3Activity.this,"Lütfen bütün alanları doldurunuz.",Toast.LENGTH_SHORT).show();
        }

        else if(et3.getText().length() != 11){

            Toast.makeText(Main3Activity.this,"Lütfen geçerli bir telefon numarası giriniz.",Toast.LENGTH_SHORT).show();
        }

        else
        {

            new AsyncTask<String,String,String>(){
                @Override
                protected String doInBackground(String... params) {

                    try {
                        Thread.sleep(2000);
                        editor.putString("name",a);
                        editor.putString("surname",b);
                        editor.putString("phone",c);

                        editor.commit();

                        Intent i = new Intent(Main3Activity.this, Main4Activity.class);
                        startActivity(i);
                        finish();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pd = new ProgressDialog(Main3Activity.this);
                    pd.setMessage("Hesabınız oluşturuluyor..");
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
}
