package com.biniyam.hands_freemoneytransfer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.biniyam.hands_freemoneytransfer.utils.Common;
import com.biniyam.hands_freemoneytransfer.utils.Crouton;
import com.biniyam.hands_freemoneytransfer.utils.InputValidator;
import com.biniyam.hands_freemoneytransfer.utils.UssdHelper;

import java.util.Timer;
import java.util.TimerTask;

public class SendMoney extends AppCompatActivity {

    EditText amount, pin;
    String reciversPhone = "";
    Button submit;
    ImageButton openContact;
    TextView covid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get extra string
        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            reciversPhone = intent.getStringExtra("phone");
        } else {
            Toast.makeText(this, "Invalid QR. Scan again", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(SendMoney.this, QR_ScannerActivity.class);
            startActivity(i);
        }



        //init views


        amount = findViewById(R.id.send_ammount);
        pin = findViewById(R.id.send_pin);
        submit = findViewById(R.id.submit);
        covid= findViewById(R.id.covid_btn);

        initCrouton();
        covid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SendMoney.this, AboutCorona.class);
                startActivity(i);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String phone = reciversPhone;
                String ammo = amount.getText().toString();
                String p = pin.getText().toString();

                InputValidator validator = new InputValidator(SendMoney.this);
                if (!validator.isEmpity(amount) &&
                        !validator.isEmpity(pin)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(SendMoney.this,
                                    Manifest.permission.CALL_PHONE)) {
                                Toast.makeText(SendMoney.this, "Call permission is required", Toast.LENGTH_SHORT).show();
                            } else {
                                // No explanation needed; request the permission
                                ActivityCompat.requestPermissions(SendMoney.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        112);


                            }
                        } else {
                            String ussdString = "*847*" + "1" + "*" + phone + "*" +
                                    ammo + "*" + p + "*" + "#";

                            makeCall(ussdString);

                        }
                    }else{
                        //for older phones
                        String ussdString = "*847*" + "1" + "*" + phone + "*" +
                                ammo + "*" + p + "*" + "#";

                        makeCall(ussdString);
                    }
                }


            }
        });


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

    @SuppressLint("MissingPermission")
    private void makeCall(String ussdString) {
        UssdHelper ussd = new UssdHelper();
        freezButton();

        Intent i = new Intent(Intent.ACTION_CALL, ussd.ussdtocallable(ussdString));
        startActivity(i);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 112: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Context mAppContext = SendMoney.this;


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
