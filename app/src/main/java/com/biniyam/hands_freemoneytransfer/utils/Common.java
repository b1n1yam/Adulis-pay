package com.biniyam.hands_freemoneytransfer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.biniyam.hands_freemoneytransfer.Retrofit.API;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class Common {

    public final static int btnDelay = 5000;
    public final static String CBE_SMS_STRUCTURE = "Dear Customer,your CBE Birr account balance is";
    public final static int AWASH_SMS_STRUCTURE = 5000;
    public final static String ORO_CASH_SMS_STRUCTURE = "Avail. bal.is";
    public final static String CBE_CONTACT = "CBE Birr";
    public final static String ORO_CONTACT = "Oro Cash";
    public  final static String NAME = "ActiveBank", KEY = "bank";


    public static API getApi() {
        return com.biniyam.hands_freemoneytransfer.Retrofit.RetrofitClient.getInstance().create(API.class);
    }

    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    public static void getSms(Context context, String bankName) {
        StringBuilder smsBuilder = new StringBuilder();
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_ALL = "content://sms/";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Cursor cur = context.getContentResolver().query(uri, projection, "address='"+bankName+"'", null, "date desc");
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");

                do {
                    //TODO: fetchother notifications from messages such as received money....
                   /* String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);*/
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                   /* int int_Type = cur.getInt(index_Type);*/
                    if ( strbody.contains(CBE_SMS_STRUCTURE)) {
                        String balance = scrapeCbeBirrMessage(strbody);
                        saveToStorage(context,balance,longDate, bankName);
                        break;
                    }


                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
    }

    public static void saveToStorage(Context context,String balance, long date, String key) {
        SharedPreferences sp = context.getSharedPreferences("com.biniyam.hands_freemoneytransfer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, balance);
        editor.putString(key+"_updated", String.valueOf(date));
        editor.apply();

    }

    public static String scrapeCbeBirrMessage(String msg){
        return msg.replace("Dear Customer,your CBE Birr account balance is", "")
                .replace(". Thank You!", "").trim();
    }

    public static String scrapeOroCashMessage(String msg){
        return msg.substring(msg.indexOf("Avail. bal.is") +18 , msg.indexOf("CR as on ") );
    }


    public static  String decodePhone(String base64) throws UnsupportedEncodingException {
        // sender side
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        String text = new String(data, "UTF-8");
        return text;
    }
    public static boolean validPhoneString(String phone){
      return   phone.length() == 10 &&  phone.substring(0,2).equals("09") ||
                phone.length() == 13 &&  phone.substring(0,4).equals("+251");
    }
}
