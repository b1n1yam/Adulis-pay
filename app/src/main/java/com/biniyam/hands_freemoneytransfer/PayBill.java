package com.biniyam.hands_freemoneytransfer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.biniyam.hands_freemoneytransfer.utils.Common;
import com.biniyam.hands_freemoneytransfer.utils.Crouton;
import com.biniyam.hands_freemoneytransfer.utils.InputValidator;
import com.biniyam.hands_freemoneytransfer.utils.ThemeColors;
import com.biniyam.hands_freemoneytransfer.utils.UssdHelper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class PayBill extends AppCompatActivity {

    EditText shortCode,reference,pin;
    Button submit;
    TextView covid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(Common.NAME, Context.MODE_PRIVATE);
        final int bank = sharedPreferences.getInt(Common.KEY, 0);

        setTheme(ThemeColors.chooseTheme(bank));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bill);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final String menuId="5";
        final String selectShortcode="2";//selecting short code

        shortCode=(EditText)findViewById(R.id.bill_shortcode);
        reference=(EditText)findViewById(R.id.bill_reference);
        pin=(EditText) findViewById(R.id.bill_pin);
        submit=(Button) findViewById(R.id.submit);
        covid= findViewById(R.id.covid_btn);

        initCrouton();
        covid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PayBill.this, AboutCorona.class);
                startActivity(i);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {

                String firstArg,secondArg,getPin;
                firstArg=shortCode.getText().toString();
                secondArg=reference.getText().toString();
                getPin=pin.getText().toString();


                InputValidator validator= new InputValidator(PayBill.this);

                if (!validator.isEmpity(shortCode)&&!validator.isEmpity(reference)&&
                        !validator.isEmpity(pin)) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(PayBill.this,
                                    Manifest.permission.CALL_PHONE)) {
                                Toast.makeText(PayBill.this, "Call permission is required", Toast.LENGTH_SHORT).show();
                            } else {
                                // No explanation needed; request the permission
                                ActivityCompat.requestPermissions(PayBill.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        112);


                            }
                        } else {
                            //if purmission is already granted
                            String ussdString = "*847*" + menuId + "*" + selectShortcode + "*" + firstArg + "*" +
                                    secondArg + "*" + getPin + "#";
                            makeCall(ussdString);

                        }
                    }
                    else{
                        //ifor older phones
                        String ussdString = "*847*" + menuId + "*" + selectShortcode + "*" + firstArg + "*" +
                                secondArg + "*" + getPin + "#";
                        makeCall(ussdString);
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
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
                    Context mAppContext= PayBill.this;


                    Toast.makeText(mAppContext, "Please try again", Toast.LENGTH_SHORT).show();

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
    private void initCrouton() {
        //crouton
        CardView crouton;
        TextView bank;
        ImageView close;

        crouton= findViewById(R.id.crouton);
        bank =findViewById(R.id.bank);
        close =findViewById(R.id.close);

        final Crouton croutonCreator=new Crouton(this,crouton,bank,close);
        croutonCreator.setBank();
        croutonCreator.animateInCard();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                croutonCreator.closeCrouton();
            }
        });
    }
}
