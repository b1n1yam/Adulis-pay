package com.biniyam.hands_freemoneytransfer.utils;

import android.net.Uri;

public class UssdHelper {



    public Uri ussdtocallable(String ussd) {

        String uriStirng="";

        if (!ussd.startsWith("tel: "))
            uriStirng +="tel:";
        for(char c: ussd.toCharArray()){
            if(c=='#')
                uriStirng+=Uri.encode("#");
            else
                uriStirng +=c;
        }
        return Uri.parse(uriStirng);
    }
}
