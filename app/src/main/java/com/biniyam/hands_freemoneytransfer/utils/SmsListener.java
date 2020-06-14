package com.biniyam.hands_freemoneytransfer.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsListener extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
            Toast.makeText(context, "sms recived", Toast.LENGTH_SHORT).show();

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Log.d("messageee recieved","ollo");
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
//                        If message is from cbe
                        assert msg_from != null;
                        if(msg_from.equals(Common.CBE_CONTACT) && msgBody.contains(Common.CBE_SMS_STRUCTURE)){
                            Log.i("instant-msg-recived",msgs[i].toString());
                            //Common.saveToStorage(context,Common.scrapeCbeBirrMessage(msgBody));
                        }
                    }
                }catch(Exception e){
                         Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }
}