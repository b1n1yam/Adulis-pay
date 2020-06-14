package com.biniyam.hands_freemoneytransfer;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.biniyam.hands_freemoneytransfer.IntroFiles.intro1;
import com.biniyam.hands_freemoneytransfer.IntroFiles.intro2;
import com.biniyam.hands_freemoneytransfer.IntroFiles.intro3;
import com.biniyam.hands_freemoneytransfer.IntroFiles.intro4;
import com.biniyam.hands_freemoneytransfer.IntroFiles.intro5;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import io.reactivex.annotations.Nullable;

public class Intro extends AppIntro implements ISlideBackgroundColorHolder,
        intro1.OnFragmentInteractionListener, intro2.OnFragmentInteractionListener, intro3.OnFragmentInteractionListener,
        intro4.OnFragmentInteractionListener, intro5.OnFragmentInteractionListener {

    CharSequence title1, title2, title3, title4, title5;
    String description1, description2, description3, description4, description5;
    int image1, image2, image3, image4, image5;
    int backgroundColor1, backgroundColor2, backgroundColor3, backgroundColor4, backgroundColor5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        askForPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE}, 4);


        intro5 intro = new intro5();
        intro1 intro1 = new intro1();
        intro2 intr2 = new intro2();
        intro3 intr3 = new intro3();
        intro4 intr4 = new intro4();


        addSlide(intro);
        addSlide(intro1);
        addSlide(intr2);
        addSlide(intr3);
        addSlide(intr4);
        //addSlide(AppIntroFragment.newInstance(title4, description4, image4, backgroundColor4));


        // Override bar/separator color.

        setSeparatorColor(Color.parseColor("#ffffff"));
        showStatusBar(false);
        showSkipButton(false);


    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

    @Override
    public int getDefaultBackgroundColor() {
        return Color.parseColor("#000000");
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
}