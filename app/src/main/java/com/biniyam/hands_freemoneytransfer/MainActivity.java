package com.biniyam.hands_freemoneytransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences("com.biniyam.hands_freemoneytransfer", Context.MODE_PRIVATE);

        //start the introdction slide on first time install
        if (!sp.getBoolean("first", false)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("first", true);
            editor.apply();
            Intent i = new Intent(this, Intro.class);
            startActivity(i);

        } else {
            //check for language
            if(sp.getString("lang", "").equals("am")){
                //set amharic
                setLocale("am");
            }
            else{
                setLocale("en");
            }

            Intent i = new Intent(this, GridLayout.class);
            startActivity(i);
        }

    }
    public  void  setLocale(String localeName) {

            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

        }
    }

