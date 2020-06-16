package com.biniyam.hands_freemoneytransfer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.biniyam.hands_freemoneytransfer.utils.Common;
import com.biniyam.hands_freemoneytransfer.utils.Crouton;
import com.biniyam.hands_freemoneytransfer.utils.InputValidator;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.UnsupportedEncodingException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QR_ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private BottomSheetBehavior sheetBehavior;
    private CardView optionButton;
    private LinearLayout bottom_sheet;
    private TextView errText;
    private Button next;
    private EditText phone;
    private ImageButton contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr__scanner);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //init
        scannerView = findViewById(R.id.zxscan);
        optionButton = findViewById(R.id.option_phone_btn);
        bottom_sheet = findViewById(R.id.phone_bottom_sheet);
        errText = findViewById(R.id.phone_err_text);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        next = findViewById(R.id.option_btn_next);
        phone = findViewById(R.id.option_phone);
        contacts = findViewById(R.id.option_contacts);



        //Request permission
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(QR_ScannerActivity.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(QR_ScannerActivity.this, "Camer perrmission is required", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();

        // set callback for changes
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        scannerView.stopCamera();


                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        scannerView.startCamera();
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        // set listener on button click
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errText.setVisibility(View.GONE);//start fresh

                InputValidator validator = new InputValidator(QR_ScannerActivity.this);
                String phoneString = phone.getText().toString();
                if (!validator.isEmpity(phone) && phoneString.length() == 10 || phone.length() == 13) {
                    //da ya thing
                    Intent i = new Intent(QR_ScannerActivity.this, SendMoney.class);
                    i.putExtra("phone", phone.getText().toString());
                    startActivity(i);
                } else {
                    errText.setText("Invalid Phone Number");
                    errText.setVisibility(View.VISIBLE);
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
                startActivityForResult(i, 47);
            }

        });

    }




    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        scannerView.startCamera();
        super.onResume();
    }

    @Override
    public void handleResult(Result rawResult) {

        String phone = "";
        //check if the string is of base 64
        try {
            phone = Common.decodePhone(rawResult.getText());//decode
            Toast.makeText(this, phone, Toast.LENGTH_SHORT).show();
            if (!phoneValidator(phone))
               { Toast.makeText(this, "Invalid QR code", Toast.LENGTH_SHORT).show();
                   Intent i = new Intent(QR_ScannerActivity.this, GridLayout.class);
                   startActivity(i);
               }

            else {
                //close this activity and populate the result
                Intent i = new Intent(QR_ScannerActivity.this, SendMoney.class);
                i.putExtra("phone", phone);
                startActivity(i);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid QR code", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean phoneValidator(String phone) {
        return PhoneNumberUtils.isGlobalPhoneNumber(phone);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(QR_ScannerActivity.this, GridLayout.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 47:

                try {
                    if (resultCode == Activity.RESULT_OK) {
                        Uri contactData = data.getData();

                        assert contactData != null;
                        Cursor c = getContentResolver().query(contactData, null, null, null, null);
                        assert c != null;
                        c.moveToFirst();


                        String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        int phoneNumIndx = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String phoneNum = c.getString(phoneNumIndx).trim();
                        if (name == null) {
                            name = "No Name";

                        }

                        phone.setText(phoneNum);
                        Toast.makeText(QR_ScannerActivity.this, name + ": " + phoneNum, Toast.LENGTH_SHORT).show();


                        c.close();

                    }
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());

                }

                break;
        }
    }

}
