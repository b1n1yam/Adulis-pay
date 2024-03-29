package com.biniyam.hands_freemoneytransfer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.biniyam.hands_freemoneytransfer.utils.Common;
import com.biniyam.hands_freemoneytransfer.utils.Crouton;
import com.biniyam.hands_freemoneytransfer.utils.InputValidator;
import com.biniyam.hands_freemoneytransfer.utils.ThemeColors;
import com.biniyam.hands_freemoneytransfer.utils.USSDService;
import com.biniyam.hands_freemoneytransfer.utils.UssdHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.Timer;
import java.util.TimerTask;

public class Balance extends AppCompatActivity {

    EditText pin;
    Button submit;
    String getPin = "";
    TextView covid;
    ScrollView holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(Common.NAME, Context.MODE_PRIVATE);
        final int bank = sharedPreferences.getInt(Common.KEY, 0);

        setTheme(ThemeColors.chooseTheme(bank));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pin = findViewById(R.id.balance_pin);
        submit = findViewById(R.id.submit);
        covid = findViewById(R.id.covid_btn);
        holder = findViewById(R.id.parent_holder);

        initCrouton();
        if (bank == 2) {
            if (!isAccessServiceEnabled()) {
                Snackbar snackbar = Snackbar.make(holder, "Please enable accessibility setting", Snackbar.LENGTH_INDEFINITE).
                        setAction("Allow Accessibility", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                    startActivityForResult(intent, 0);
                                } catch (Exception e) {
                                    Toast.makeText(Balance.this, "Unable to start settings. Please go to settings > asccessiblity > "
                                            + getResources().getString(R.string.app_name)
                                            + " and enable accessibility", Toast.LENGTH_SHORT).show();
                                    Log.e("SETTING_ERROR", e.getMessage());
                                }
                            }
                        });
                snackbar.show();
            }
        }
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
                        createCallStructure(bank);

                    }
                } else {
                    createCallStructure(bank);
                }


            }
        });
    }

    public boolean isAccessServiceEnabled() {
        String prefString = Settings.Secure.getString(Balance.this.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

        return prefString != null && prefString.contains(Balance.this.getPackageName() + "/" + USSDService.class.getName());
    }

    private void createCallStructure(int bank) {
        switch (bank) {
            case 0:
                cbeRequest();
                break;
            case 1:
                awashReuest();
                break;

            default:
                oroRequest();
                break;

        }
    }

    private void oroRequest() {
        InputValidator validator = new InputValidator(Balance.this);
        //check for validation
        if (!validator.isEmpity(pin)) {
            String ussdString = "*840*" + getPin + "*" + "5" + "*" + "1" + "#";
            makeCall(ussdString);
        }
    }

    private void cbeRequest() {
        InputValidator validator = new InputValidator(Balance.this);
        //check for validation
        if (!validator.isEmpity(pin)) {
            String ussdString = "*847*" + "6" + "*" + getPin + "#";
            makeCall(ussdString);
        }
    }

    private void awashReuest() {
        Toast.makeText(this, "ToDo: awash reuest", Toast.LENGTH_SHORT).show();
    }

    private void makeCall(String ussdString) {
        UssdHelper ussd = new UssdHelper();
        freezButton();
        Intent i = new Intent(Intent.ACTION_CALL, ussd.ussdtocallable(ussdString));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
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
                    Context mAppContext = Balance.this;
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

    private void initCrouton() {
        //crouton
        CardView crouton;
        TextView bank;
        ImageView close;

        crouton = findViewById(R.id.crouton);
        bank = findViewById(R.id.bank);
        close = findViewById(R.id.close);

        final Crouton croutonCreator = new Crouton(this, crouton, bank, close);
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
