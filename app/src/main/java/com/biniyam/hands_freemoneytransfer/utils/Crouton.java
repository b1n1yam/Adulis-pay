package com.biniyam.hands_freemoneytransfer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.biniyam.hands_freemoneytransfer.QR_ScannerActivity;
import com.biniyam.hands_freemoneytransfer.R;

import java.util.Timer;
import java.util.TimerTask;

public class Crouton {
    private Context context;
    private CardView crouton;
    private TextView bankName;
    private ImageView close;
    SharedPreferences sharedPreferences;
    private final int ANIMATION_DURATION = 600;
    public Crouton(Context context, CardView crouton, TextView bankName, ImageView close) {
        this.context = context;
        this.crouton = crouton;
        this.bankName = bankName;
        this.close = close;
    }

    public void animateInCard(){

        Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_top);
        anim.setDuration(ANIMATION_DURATION);
        crouton.startAnimation(anim);

    }

    public void closeCrouton(){
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_back_up);
        anim.setDuration(ANIMATION_DURATION);
        crouton.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                crouton.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    public void setBank(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Common.NAME, Context.MODE_PRIVATE);
        final int bank = sharedPreferences.getInt(Common.KEY, 0);
        bankName.setTextColor(ThemeColors.getAccent(context));
        switch (bank){
            case 0:
                bankName.setText("CBE Birr");
                break;
            case 1:
                bankName.setText("M-Wallet");
                break;
            case 2:
                bankName.setText("Oro-Cash");
        }

    }


}
