package com.biniyam.hands_freemoneytransfer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.biniyam.hands_freemoneytransfer.utils.Common;
import com.biniyam.hands_freemoneytransfer.utils.Crouton;
import com.biniyam.hands_freemoneytransfer.utils.InputValidator;
import com.biniyam.hands_freemoneytransfer.utils.ThemeColors;
import com.biniyam.hands_freemoneytransfer.utils.UssdHelper;

import java.util.Timer;
import java.util.TimerTask;

public class BuyAirtime extends AppCompatActivity {

    Switch mineOrOther;
    LinearLayout holder;
    EditText phoneNo, ammount, pin;
    Button submit;
    String firstArg = "1", secondArg, getPhone, getPin;
    CheckBox savepass;
    ImageButton contacts;
    private Animation animationUp;
    private Animation animationDown;
    TextView covid;
    String menuId = "3";//Indicates the menu item is buy airtime
    final String prmptToPhone = "1";//Selecting enter phone number among "Select Beneficiary"
    private static final String NAME = "ActiveBank", KEY = "bank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = this.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        final int bank = sharedPreferences.getInt(KEY, 0);

        setTheme(ThemeColors.chooseTheme(bank));
        super.onCreate(savedInstanceState);

        //change theme
        setContentView(R.layout.activity_buy_airtime);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initializing animations
        animationUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        Toast.makeText(this, String.valueOf(bank), Toast.LENGTH_SHORT).show();

        //initializing views
        mineOrOther = findViewById(R.id.mine_or_other);
        phoneNo = findViewById(R.id.other_phoneno);
        ammount = findViewById(R.id.balance_ammount);
        pin = findViewById(R.id.air_pin);
        submit = findViewById(R.id.submit);
        holder = findViewById(R.id.other_phone_holder);
        contacts = findViewById(R.id.open_contact_to_air);
        covid = findViewById(R.id.covid_btn);

        initCrouton();
        covid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BuyAirtime.this, AboutCorona.class);
                startActivity(i);
            }
        });

        holder.setVisibility(View.INVISIBLE);


        mineOrOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mineOrOther.isChecked()) {
                    holder.setVisibility(View.VISIBLE);
                    holder.startAnimation(animationDown);
                    firstArg = "2";
                    //Log.i("first",firstArg);

                } else if (!mineOrOther.isChecked()) {
                    holder.setVisibility(View.INVISIBLE);
                    holder.startAnimation(animationUp);
                    firstArg = "1";
                    //Log.i("first",firstArg);

                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                secondArg = ammount.getText().toString();
                getPin = pin.getText().toString();


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(BuyAirtime.this,
                                Manifest.permission.CALL_PHONE)) {
                            Toast.makeText(BuyAirtime.this, "Call permission is required", Toast.LENGTH_SHORT).show();
                        } else {
                            // No explanation needed; request the permission
                            ActivityCompat.requestPermissions(BuyAirtime.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    112);


                        }
                    } else {
                        //if purmission is already granted
                        createCallStructure(bank);
                    }
                } else {
                    //for older phone
                    createCallStructure(bank);
                }

            }

        });
        //open contact to send blance to
        contacts.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i, 1);
            }

        });

    }

    private void createCallStructure(int bank) {

        switch (bank) {
            case 0:
                cbeRequest();
                break;
            case 1:
                awashRequest();
                break;
            case 2:
                abayRequest();
                break;
        }

    }

    private void abayRequest() {
        menuId ="3";
        InputValidator validator = new InputValidator(BuyAirtime.this);

        String ussdString = "";
        if (firstArg.equals("1")) {


            if (!validator.isEmpity(ammount) &&
                    !validator.isEmpity(pin)) {

                ussdString = "*840*" +getPin+"*"+ menuId + "*" + firstArg + "*" +
                        secondArg + "*" +"1"+ "#";

                makeCall(ussdString);

            }
        } else if (firstArg.equals("2")) {

            if (!validator.isEmpity(phoneNo) && !validator.isEmpity(ammount) &&
                    !validator.isEmpity(pin)) {
                getPhone = phoneNo.getText().toString();
                ussdString = "*840*" +getPin+ "*" + menuId + "*" + firstArg  + "*"
                        + getPhone + "*" + secondArg + "*" + "1" + "#";

                makeCall(ussdString);

            }
        }
    }

    private void awashRequest() {
        Toast.makeText(this, "awash transfer", Toast.LENGTH_SHORT).show();
    }

    private void cbeRequest() {
        InputValidator validator = new InputValidator(BuyAirtime.this);

        String ussdString = "";
        if (firstArg.equals("1")) {


            if (!validator.isEmpity(ammount) &&
                    !validator.isEmpity(pin)) {

                ussdString = "*847*" + menuId + "*" + firstArg + "*" +
                        secondArg + "*" + getPin + "#";

                makeCall(ussdString);

            }
        } else if (firstArg.equals("2")) {

            if (!validator.isEmpity(phoneNo) && !validator.isEmpity(ammount) &&
                    !validator.isEmpity(pin)) {
                getPhone = phoneNo.getText().toString();
                ussdString = "*847*" + menuId + "*" + firstArg + "*" + prmptToPhone + "*"
                        + getPhone + "*" + secondArg + "*" + getPin + "#";

                makeCall(ussdString);

            }
        }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:

                try{
                    if (resultCode== Activity.RESULT_OK) {
                        Uri contactData = data.getData();

                        Cursor c = getContentResolver().query(contactData, null, null, null, null);
                        c.moveToFirst();


                        String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        int phoneNumIndx = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String phoneNum = c.getString(phoneNumIndx);
                        if (name == null) {
                            name = "No Name";

                        }



                        phoneNo.setText(phoneNum);
                        Toast.makeText(BuyAirtime.this,name+": "+phoneNum,Toast.LENGTH_SHORT).show();


                        c.close();

                    }
                }catch(Exception e){
                    Log.e("Error",e.getMessage());

                }

                break;
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 112: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Context mAppContext= BuyAirtime.this;


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
