package com.biniyam.hands_freemoneytransfer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.biniyam.hands_freemoneytransfer.utils.Common;
import com.biniyam.hands_freemoneytransfer.utils.InputValidator;
import com.biniyam.hands_freemoneytransfer.utils.UssdHelper;

import java.util.Timer;
import java.util.TimerTask;

public class Balance extends AppCompatActivity {

    EditText pin;
    Button submit;
    String getPin = "";
    TextView covid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pin = findViewById(R.id.balance_pin);
        submit = findViewById(R.id.submit);
        covid= findViewById(R.id.covid_btn);
        covid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Balance.this, AboutCorona.class);
                startActivity(i);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                getPin = pin.getText().toString();

                InputValidator validator = new InputValidator(Balance.this);
                String ussdString = "*847*" + "6" + "*" + getPin + "#";


                //check for validation
                if (!validator.isEmpity(pin)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        //check for permission
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(Balance.this,
                                    Manifest.permission.CALL_PHONE)) {
                                Toast.makeText(Balance.this, "Call permission is required", Toast.LENGTH_SHORT).show();
                            } else {
                                // No explanation needed; request the permission
                                ActivityCompat.requestPermissions(Balance.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        112);


                            }
                        } else {
                            makeCall(ussdString);

                        }
                    }else{
                        makeCall(ussdString);
                    }

                }
            }
        });
    }
    private void makeCall(String ussdString) {
        UssdHelper ussd = new UssdHelper();
        freezButton();
        Intent i = new Intent(Intent.ACTION_CALL, ussd.ussdtocallable(ussdString));
        startActivity(i);
    }
    private void freezButton() {
        submit.setEnabled(false);
        submit.setBackgroundColor(Color.parseColor("#A5D6A7"));
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        submit.setEnabled(true);
                        submit.setBackgroundColor(Color.parseColor("#4ca047"));

                    }
                });
            }
        }, Common.btnDelay);
    }


    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 112: {
                
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Context mAppContext= Balance.this;
                    String ussdString = "*847*" + "6" + "*" + getPin + "#";

                    makeCall(ussdString);

                } else {
                    Toast.makeText(this, "You cannot perform this action without accepting the required permission ", Toast.LENGTH_SHORT).show();
                    //btn_recive.setVisibility(View.GONE);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}