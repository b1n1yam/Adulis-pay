package com.biniyam.hands_freemoneytransfer.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import net.glxn.qrgen.android.QRCode;

import java.io.UnsupportedEncodingException;

public class GenerateQrCode {


    public Bitmap generateQr(String phone) throws UnsupportedEncodingException {

        return QRCode.from(encodePhone(phone)).bitmap();

    }

    public  String encodePhone(String phone) throws UnsupportedEncodingException {
        // reciver side
        byte[] data = phone.getBytes("UTF-8");
        return Base64.encodeToString(data, Base64.DEFAULT);
    }


}
