package com.biniyam.hands_freemoneytransfer.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.biniyam.hands_freemoneytransfer.GridLayout;
import com.biniyam.hands_freemoneytransfer.R;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.text.format.DateUtils.FORMAT_ABBREV_ALL;
import static android.text.format.DateUtils.MINUTE_IN_MILLIS;
import static android.text.format.DateUtils.getRelativeTimeSpanString;

class CategoryAdapter extends RecyclerView.ViewHolder {

    protected ImageView walletLogo;
    protected CardView walletCard;
    protected TextView walletPhone;
    protected TextView lastUpdate;
    protected TextView balance;
    protected TextView walletName;
    protected TextView lastUpdateString;


    public CategoryAdapter(@NonNull View itemView) {
        super(itemView);

        walletLogo = (ImageView) itemView.findViewById(R.id.wallet_logo);
        walletCard = (CardView) itemView.findViewById(R.id.wallet_card);
        walletPhone = (TextView) itemView.findViewById(R.id.wallet_phone);
        lastUpdate = (TextView) itemView.findViewById(R.id.last_update);
        walletName= (TextView) itemView.findViewById(R.id.wallet_name);
        lastUpdateString= (TextView) itemView.findViewById(R.id.last_update_string);
        balance= (TextView) itemView.findViewById(R.id.wallet_ballance);


    }


}

public class BankAdapter extends RecyclerView.Adapter<CategoryAdapter> {

    Context context;
    List<Integer> cardBg = new ArrayList<>();
    List<String> walletNames = new ArrayList<>();
    List<String> lastUpdates = new ArrayList<>();
    List<String> balances = new ArrayList<>();
    List<Boolean> balanceExist = new ArrayList<>();
    List<Integer> walletLogo = new ArrayList<>();

    public BankAdapter(Context context, List<Integer> cardBg, List<String> walletNames, List<String> lastUpdates, List<String> balances, List<Boolean> balanceExist, List<Integer> walletLogo) {
        this.context = context;
        this.cardBg = cardBg;
        this.walletNames = walletNames;
        this.lastUpdates = lastUpdates;
        this.balances = balances;
        this.balanceExist= balanceExist;
        this.walletLogo = walletLogo;
    }

    @NonNull
    @Override
    public CategoryAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.item_bank, parent, false);


        return new CategoryAdapter(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter holder, int i) {



        holder.walletName.setText(walletNames.get(i));
        holder.walletCard.setCardBackgroundColor(cardBg.get(i));
        if(!balanceExist.get(i)){
            holder.lastUpdateString.setText("Please check your balance");
            holder.lastUpdate.setVisibility(View.GONE);
            holder.balance.setVisibility(View.GONE);
        }
        else{
            //String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(String.valueOf(lastUpdates.get(i))));
            long date= Long.parseLong(lastUpdates.get(i));
            String dateRelative = (String) getRelativeTimeSpanString( date,
                    System.currentTimeMillis(),MINUTE_IN_MILLIS,FORMAT_ABBREV_ALL);
            holder.lastUpdate.setText(dateRelative);
            holder.balance.setText(balances.get(i));
        }
        holder.walletLogo.setImageDrawable(BgCenter(walletLogo.get(i)));
        //holder.walletCard.setImageDrawable(BgCenter(images.get(i)));


    }

    @Override
    public int getItemCount() {
        return walletNames.size();
    }

    public BitmapDrawable BgCenter(int rid){
        Bitmap bgBit= BitmapFactory.decodeResource(context.getResources(),rid);
        BitmapDrawable bgDrawable=new BitmapDrawable(context.getResources(),bgBit);
        bgDrawable.setGravity(Gravity.CENTER);


        return  bgDrawable;

    }
}