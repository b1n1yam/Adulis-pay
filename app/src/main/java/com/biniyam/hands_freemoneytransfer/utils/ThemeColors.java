package com.biniyam.hands_freemoneytransfer.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.TypedValue;

import com.biniyam.hands_freemoneytransfer.R;

public class ThemeColors {
    private static final String NAME = "ThemeColors", KEY = "color";

    public static int chooseTheme (int bank){
        switch (bank){
            case 0:
                return R.style.cbe_theme;
            case 1:
                return R.style.awash_theme;
            default:
                return R.style.oro_theme;
        }

    }

    public static int getAccent(Context context){
        TypedValue typedValue =new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
        int color = a.getColor(0,0);

        a.recycle();
        return color;
    }

}