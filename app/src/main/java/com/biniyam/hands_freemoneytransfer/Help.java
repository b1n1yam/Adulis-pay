package com.biniyam.hands_freemoneytransfer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Help extends AppCompatActivity {


    Locale myLocale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lang, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.lang:
                 AlertDialog.Builder builder= new AlertDialog.Builder(Help.this);
                   builder.setTitle("Choose language");
                builder.setPositiveButton("English", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setLocale("en");

                    }
                })
                        .setNegativeButton("Amharic", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                    setLocale("am");

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

        }
        return super.onOptionsItemSelected(item);
    }

    public void setLocale(String localeName) {
        //get cuttent language
        SharedPreferences sp = getSharedPreferences("com.biniyam.hands_freemoneytransfer", Context.MODE_PRIVATE);
       String currentLanguage = sp.getString("lang", "");

       //set language
        if (!localeName.equals(currentLanguage)) {

            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            //actualy updating it
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("lang", localeName);
            editor.apply();

            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
        } else {
            Toast.makeText(Help.this, "Language already selected!", Toast.LENGTH_SHORT).show();
        }
    }
}