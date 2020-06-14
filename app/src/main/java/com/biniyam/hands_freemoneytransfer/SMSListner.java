package com.biniyam.hands_freemoneytransfer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.biniyam.hands_freemoneytransfer.utils.Common;

public class SMSListner extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
//                        If message is from cbe
                        assert msg_from != null;

                        Intent in = new Intent("com.biniyam.hands_freemoneytransfer");
                        Bundle extras = new Bundle();

                        if (msg_from.equals(Common.CBE_CONTACT) && msgBody.contains(Common.CBE_SMS_STRUCTURE)) {
                            Common.saveToStorage(context, Common.scrapeCbeBirrMessage(msgBody), System.currentTimeMillis(),
                                    Common.CBE_CONTACT);

                            extras.putString("com.biniyam.hands_freemoneytransfer.balance", Common.CBE_CONTACT);
                            in.putExtras(extras);
                            context.sendBroadcast(in);
                        }
                        //TODO: get notificcation for Awash bank as well
                        //TODO: get notificcation for Oromia bank as well



                    }
                } catch (Exception e) {
                    Log.d("Exception caught", e.getMessage());
                }
            }
        }
    }

}

