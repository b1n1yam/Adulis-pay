package com.biniyam.hands_freemoneytransfer.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.widget.EditText;
import android.widget.Toast;

public class InputValidator {


        @SuppressLint("StaticFieldLeak")
        private Context context;
        public InputValidator(Context context){
            this.context =context;

        }
        public boolean isEmpity(EditText editText){
            String input= editText.getText().toString().trim();
            if (input.length()==0){
                editText.getBackground().mutate().setColorFilter(context.getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_LONG).show();

            }

            return input.length()==0;

        }


}











































