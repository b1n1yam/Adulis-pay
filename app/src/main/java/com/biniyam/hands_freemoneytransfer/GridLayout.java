package com.biniyam.hands_freemoneytransfer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.biniyam.hands_freemoneytransfer.Adapter.BankAdapter;
import com.biniyam.hands_freemoneytransfer.utils.CapturePhotoUtils;
import com.biniyam.hands_freemoneytransfer.utils.Common;
import com.biniyam.hands_freemoneytransfer.utils.GenerateQrCode;
import com.biniyam.hands_freemoneytransfer.utils.InputValidator;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class GridLayout extends AppCompatActivity {

    private static final String NAME = "ActiveBank", KEY = "bank";
    private final int ANIM_INIT = 400;
    String mPhoneNumber = "";
    Context mAppContext = GridLayout.this;
    GenerateQrCode qrCode = new GenerateQrCode();
    private CardView c1R1, c2R1, c1R2, c2R2, c1R3,  btn_send, btn_recive;
    private Animation anim1, anim2, anim3, anim4, anim5, anim6, anim7, anim8, anim9, anim10, anim11, anim12;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet, coronaBtn, qrContent, setPhoneContent, loadingView;
    private ImageView qrImage;
    private LinearLayout saveImage;
    private RelativeLayout parentHolder;
    private Snackbar snackbar;
    private ImageView saveImageIcon;
    private Button setPhone;
    private EditText phone;
    private BroadcastReceiver broadcastReceiver;
    private ArrayList<CardView> cards = new ArrayList<>();
    private int animStart, animVar = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //new ThemeColors(this);

        setContentView(R.layout.activity_grid_layout);

        registerReceiver();
        c1R1 = findViewById(R.id.c1r1);
        c2R1 = findViewById(R.id.c2r1);
        c1R2 = findViewById(R.id.c1r2);
        c2R2 = findViewById(R.id.c2r2);


        cards.add(c1R1);
        cards.add(c2R1);
        cards.add(c1R2);
        cards.add(c2R2);

        btn_send = findViewById(R.id.btn_send);
        btn_recive = findViewById(R.id.btn_recive);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        qrImage = findViewById(R.id.qr_image);
        saveImage = findViewById(R.id.save_image);
        saveImageIcon = findViewById(R.id.save_img_icon);
        coronaBtn = findViewById(R.id.corona_btn);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        parentHolder = findViewById(R.id.parent_holder);
        if (checkForAllPermission()) {
            generateQr();
        }


        slideAnim(btn_send, anim5, 600);
        slideAnim(btn_recive, anim4, 800);

        slideRight(coronaBtn, anim7, 600);

        renderHorizontalSlider();


        c1R1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GridLayout.this, QR_ScannerActivity.class);
                startActivity(i);
            }
        });
        c2R2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GridLayout.this, BuyAirtime.class);
                startActivity(i);
            }
        });
        c1R2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GridLayout.this, PayBill.class);
                startActivity(i);
            }
        });
        c2R1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GridLayout.this, Balance.class);
                startActivity(i);
            }
        });

        coronaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GridLayout.this, AboutCorona.class);
                startActivity(i);
            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        saveImageToGallery();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (mPhoneNumber.equals("")) {
                            SharedPreferences sp = getSharedPreferences("com.biniyam.hands_freemoneytransfer", Context.MODE_PRIVATE);

                            mPhoneNumber = sp.getString("phone", "");
                        }
                        CapturePhotoUtils.insertImage(getContentResolver(), qrCode.generateQr(mPhoneNumber), "hands-free rq - generated", "QR code exported from hands-free mobile application");
                        Toast.makeText(mAppContext, "Image saved to your gallery!", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Log.e("error: ", e.getMessage());
                        Toast.makeText(mAppContext, "Your android version doesn't support this action", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //SHOW CASE
        //check if this is the first time
        showCase("first_covid", coronaBtn, "You can get the latest COVID-19 cases regurding Ethiopia and other countries",
                1000, "covid");

        // set callback for changes
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        //check if phone number is set
                        setPhoneContent = bottomSheet.findViewById(R.id.set_phone_content);
                        qrContent = bottomSheet.findViewById(R.id.qr_content);
                        loadingView = bottom_sheet.findViewById(R.id.loading_view);
                        final TextView err = bottomSheet.findViewById(R.id.err_text);

                        final SharedPreferences sp = getSharedPreferences("com.biniyam.hands_freemoneytransfer", Context.MODE_PRIVATE);
                        if (mPhoneNumber.equals("")) {
                            //if phone has been saved before
                            if (sp.getString("phone", "").equals("")) {
                                //get fields
                                setPhoneContent.setVisibility(View.VISIBLE);
                                qrContent.setVisibility(View.GONE);
                                loadingView.setVisibility(View.GONE);


                                phone = bottomSheet.findViewById(R.id.phone);
                                setPhone = bottomSheet.findViewById(R.id.btn_set_phone);
                                setPhone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        err.setVisibility(View.GONE);//start fresh

                                        InputValidator validator = new InputValidator(GridLayout.this);
                                        String phoneString = phone.getText().toString();
                                        //check for shared preference
                                        if (!validator.isEmpity(phone) && Common.validPhoneString(phoneString)) {
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putString("phone", phoneString);
                                            editor.apply();
                                            mPhoneNumber = phoneString;
                                            renderHorizontalSlider();
                                            try {
                                                qrImage.setImageBitmap(qrCode.generateQr(mPhoneNumber));
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }

                                            setPhoneContent.setVisibility(View.GONE);
                                            qrContent.setVisibility(View.VISIBLE);
                                            loadingView.setVisibility(View.GONE);
                                        } else {
                                            err.setText("Invalid Phone Number");
                                            err.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });

                            }
                            //if phone is already stored in shared preference
                            else {
                                mPhoneNumber = sp.getString("phone", "");
                                try {
                                    qrImage.setImageBitmap(qrCode.generateQr(mPhoneNumber));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                                setPhoneContent.setVisibility(View.GONE);
                                qrContent.setVisibility(View.VISIBLE);
                                loadingView.setVisibility(View.GONE);

                                showCase("first_image", saveImageIcon,
                                        "You can save this QR image to your Gallery, print it and post it on your store front",
                                        500, "save");
                            }
                        } else {
                            setPhoneContent.setVisibility(View.GONE);
                            qrContent.setVisibility(View.VISIBLE);
                            loadingView.setVisibility(View.GONE);

                            showCase("first_image", saveImageIcon,
                                    "You can save this QR image to your Gallery, print it and post it on your store front",
                                    500, "save");
                        }
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:

                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        setPhoneContent = bottomSheet.findViewById(R.id.set_phone_content);
                        qrContent = bottomSheet.findViewById(R.id.qr_content);
                        loadingView = bottom_sheet.findViewById(R.id.loading_view);
                        if (mPhoneNumber.equals("")) {

                            setPhoneContent.setVisibility(View.VISIBLE);
                            qrContent.setVisibility(View.GONE);
                            loadingView.setVisibility(View.GONE);
                        } else {
                            setPhoneContent.setVisibility(View.GONE);
                            qrContent.setVisibility(View.VISIBLE);
                            loadingView.setVisibility(View.GONE);

                        }
                        // update collapsed button text
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
        btn_recive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GridLayout.this, QR_ScannerActivity.class);
                startActivity(i);
            }
        });

    }

    private void renderHorizontalSlider() {
        //init slider
        List<Integer> cardBg = new ArrayList<>();
        List<String> walletNames = new ArrayList<>();
        List<Integer> walletLogo = new ArrayList<>();
        List<String> lastUpdates = new ArrayList<>();
        List<String> balances = new ArrayList<>();
        List<Boolean> balanceExist = new ArrayList<>();

        cardBg.add(getResources().getColor(R.color.cbePrimary));
        cardBg.add(getResources().getColor(R.color.colorPrimary));
        walletNames.add(getResources().getString(R.string.cbe_birr));

        walletNames.add(getResources().getString(R.string.oro_cash));
        walletLogo.add(R.drawable.cbe_wallet);
        walletLogo.add(R.drawable.oro_cash);

        SharedPreferences sp = getSharedPreferences("com.biniyam.hands_freemoneytransfer", Context.MODE_PRIVATE);
        String cbeBalance = sp.getString(Common.CBE_CONTACT, "");
        String cbeUpdated = sp.getString(Common.CBE_CONTACT + "_updated", "");
        String oroBalance = sp.getString(Common.ORO_CONTACT, "");
        String oroUpdated = sp.getString(Common.ORO_CONTACT + "_updated", "");
        if (cbeBalance.equals("")) {
            lastUpdates.add("");
            balances.add("");
            balanceExist.add(false);

        } else {
            balanceExist.add(true);
            balances.add(cbeBalance);
            lastUpdates.add(cbeUpdated);

        }

        if (oroBalance.equals("")) {
            lastUpdates.add("");
            balances.add("");
            balanceExist.add(false);

        } else {
            balanceExist.add(true);
            balances.add(oroBalance);
            lastUpdates.add(oroUpdated);

        }

        Common.getSms(GridLayout.this, Common.CBE_CONTACT);


        DiscreteScrollView slider = findViewById(R.id.slider);
        BankAdapter categoryAdapter = new BankAdapter(GridLayout.this, cardBg, walletNames, lastUpdates, balances, balanceExist, walletLogo);
        slider.setAdapter(categoryAdapter);
        slider.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.0f)
                .setMinScale(0.9f)
                .setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.BOTTOM)
                .build()
        );

        SharedPreferences sharedPreferences = this.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        int bank = sharedPreferences.getInt(KEY, 0);
        slider.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int i) {
                SharedPreferences.Editor editor = mAppContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();


                if (i == 1) {
                    showOro();
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    editor.putInt(KEY, 2);
                } else {
                    showCBE();

                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cbePrimary)));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        getWindow().setStatusBarColor(getResources().getColor(R.color.cbeDark));
                    }
                    editor.putInt(KEY, i);
                }
                editor.apply();

            }
        });
    }




    private void showCBE() {
        animStart = ANIM_INIT;
        for (CardView card : cards) {
                showViews(card);
        }

    }

    private void showOro() {
        animStart = ANIM_INIT;

        for (CardView card : cards) {
            if (card == c1R1 || card == c2R1 || card == c2R2) {
                showViews(card);

            } else {
                hideViews(card);
            }
        }
    }

    private void showViews(CardView card) {
        if (card.getVisibility() != View.VISIBLE) {//check if the view is already visible
            Animation myAnim = null;
            scaleAnim(card, myAnim, animStart);
            card.setVisibility(View.VISIBLE);
            animStart += animVar;

        }
    }

    private void hideViews(CardView card) {

        if (card.getVisibility() != View.GONE) {//check if the view is already gone
            Animation myAnim = null;
            scaleDownAnim(card, myAnim, 400, card);


        }
    }


    private void registerReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String otpCode = intent.getStringExtra("com.biniyam.hands_freemoneytransfer.balance");
                renderHorizontalSlider();

            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("com.biniyam.hands_freemoneytransfer"));
    }


    private void showCase(String preference, View target, String desc, int delay, String id) {
        if (checkForAllPermission()) {
            SharedPreferences sp = getSharedPreferences("com.biniyam.hands_freemoneytransfer", Context.MODE_PRIVATE);
            if (!sp.getBoolean(preference, false)) {
                new MaterialShowcaseView.Builder(this)
                        .setTarget(target)
                        .setDismissText("GOT IT")
                        .setContentText(desc)
                        .setDelay(delay) // optional but starting animations immediately in onCreate can make them choppy
                        .singleUse(id) // provide a unique ID used to ensure it is only shown once
                        .show();
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean(preference, true);
                editor.apply();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void saveImageToGallery() throws UnsupportedEncodingException {

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(GridLayout.this,
                    Manifest.permission.CALL_PHONE)) {
                Toast.makeText(GridLayout.this, "You must grant access to your storage device inorder to save this image on your phone", Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(GridLayout.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        122);


            }
        } else {
            CapturePhotoUtils.insertImage(getContentResolver(), qrCode.generateQr(mPhoneNumber), "hands-free rq - generated", "QR code exported from hands-free mobile application");
            Toast.makeText(mAppContext, "Image saved to your gallery!", Toast.LENGTH_SHORT).show();
        }
    }


    private void generateQr() {
        //get the current users phone number
        TelephonyManager tMgr = (TelephonyManager) mAppContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(GridLayout.this,
                        Manifest.permission.READ_PHONE_STATE)) {
                    Toast.makeText(mAppContext, "Reading your phone state permission is required", Toast.LENGTH_SHORT).show();
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(GridLayout.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            111);


                }

            } else {
                // Permission has already been granted
                assert tMgr != null;
                mPhoneNumber = tMgr.getLine1Number();
                if (!mPhoneNumber.equals("")) {
                    try {
                        final SharedPreferences sp = getSharedPreferences("com.biniyam.hands_freemoneytransfer", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("phone", mPhoneNumber);
                        editor.apply();
                        qrImage.setImageBitmap(qrCode.generateQr(mPhoneNumber));
                    } catch (Exception e) {
                        Log.e("error", e.toString());
                    }
                }
            }
        }


    }


    private boolean checkForAllPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (
                    checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                snackbar = Snackbar.make(parentHolder, "Please accept all permissions", Snackbar.LENGTH_INDEFINITE).
                        setAction("Allow Permission", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Common.startInstalledAppDetailsActivity(GridLayout.this);
                            }
                        });
                snackbar.show();
                return false;
            } else {
                return true;//if all permissions are accepted
            }
        } else {
            return false;//if the phone is > API 23
        }

    }

    protected void scaleAnim(CardView gridCard, Animation anim, long duration) {
        anim = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        anim.setDuration(duration);
        gridCard.startAnimation(anim);

    }

    protected void scaleDownAnim(CardView gridCard, Animation anim, long duration, final CardView mCard) {
        anim = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        anim.setDuration(duration);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCard.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        gridCard.startAnimation(anim);


    }

    protected void slideAnim(CardView gridCard, Animation anim, long duration) {
        anim = AnimationUtils.loadAnimation(this, R.anim.slide_buttom);

        anim.setDuration(duration);
        gridCard.startAnimation(anim);

    }

    protected void slideRight(LinearLayout gridCard, Animation anim, long duration) {
        anim = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);

        anim.setDuration(duration);
        gridCard.startAnimation(anim);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 111: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager tMgr = (TelephonyManager) mAppContext.getSystemService(Context.TELEPHONY_SERVICE);
                    assert tMgr != null;
                    mPhoneNumber = tMgr.getLine1Number();
                    try {
                        qrImage.setImageBitmap(qrCode.generateQr(mPhoneNumber));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, "Receiving money requires this functionality ", Toast.LENGTH_SHORT).show();
                    btn_recive.setVisibility(View.GONE);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case 122: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        CapturePhotoUtils.insertImage(getContentResolver(), qrCode.generateQr(mPhoneNumber), "hands-free rq - generated", "QR code exported from hands-free mobile application");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(this, "Saving image to your phone requires your storage's permission ", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.help:
                Intent i = new Intent(GridLayout.this, Help.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (broadcastReceiver != null) {
                unregisterReceiver(broadcastReceiver);
            }
        }catch(Exception e){
            Log.w("unregistered receiver",e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderHorizontalSlider();
    }
}


