package com.biniyam.hands_freemoneytransfer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.biniyam.hands_freemoneytransfer.Retrofit.API;
import com.biniyam.hands_freemoneytransfer.Retrofit.Country;
import com.biniyam.hands_freemoneytransfer.utils.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutCorona extends AppCompatActivity {

    private final String token = "1df33ed1b6msh086be09c6ab6e53p1222b5jsnfc6453f7b8aa";
    private final String host = "coronavirus-monitor.p.rapidapi.com";
    TextView newCases, activeCases, deathCases;
    TextView newCases1, activeCases1, deathCases1, country1;
    TextView newCases2, activeCases2, deathCases2, country2;
    TextView newCases3, activeCases3, deathCases3, country3;
    RelativeLayout main;
    LinearLayout noInternet, loading;
    Button tryAgain;
    private API api;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_corona);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        api = Common.getApi();

        //INIT VIEWS
        newCases = findViewById(R.id.new_cases);
        activeCases = findViewById(R.id.active_cases);
        deathCases = findViewById(R.id.death_cases);

        newCases1 = findViewById(R.id.new_cases1);
        activeCases1 = findViewById(R.id.active_cases1);
        deathCases1 = findViewById(R.id.death_cases1);
        country1 = findViewById(R.id.country1);

        newCases2 = findViewById(R.id.new_cases2);
        activeCases2 = findViewById(R.id.active_cases2);
        deathCases2 = findViewById(R.id.death_cases2);
        country2 = findViewById(R.id.country2);

        newCases3 = findViewById(R.id.new_cases3);
        activeCases3 = findViewById(R.id.active_cases3);
        deathCases3 = findViewById(R.id.death_cases3);
        country3 = findViewById(R.id.country3);


        main = findViewById(R.id.main_content);
        noInternet = findViewById(R.id.no_ntw_content);
        loading = findViewById(R.id.loading);
        tryAgain = findViewById(R.id.btn_try_again);
        refreshLayout = findViewById(R.id.swipe_container);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkNewtworkStatus();
            }
        });

        checkNewtworkStatus();

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNewtworkStatus();
            }
        });
    }

    private void checkNewtworkStatus() {
        if (Common.checkConnection(AboutCorona.this)) {
            loadData();
        } else {
            showNoNtwContent();
        }
    }

    private void showNoNtwContent() {
        refreshLayout.setRefreshing(false);
        loading.setVisibility(View.GONE);
        main.setVisibility(View.GONE);
        noInternet.setVisibility(View.VISIBLE);

    }

    private void loadData() {
        loading.setVisibility(View.VISIBLE);
        noInternet.setVisibility(View.GONE);
        main.setVisibility(View.GONE);
        refreshLayout.setRefreshing(true);

        getData("Ethiopia", token, host, newCases, activeCases, deathCases);
        getData("Italy", token, host, newCases1, activeCases1, deathCases1);
        getData("USA", token, host, newCases2, activeCases2, deathCases2);
        getData("Iran", token, host, newCases3, activeCases3, deathCases3);


    }

    public void getData(final String country, String token, String host, final TextView newCases, final TextView activeCases, final TextView deathCases) {
        Call<Country> call = api.getCoronaByCountry(country, token, host);
        call.enqueue(new Callback<Country>() {
            @Override
            public void onResponse(Call<Country> call, Response<Country> response) {
                if (response.body() != null) {


                    if (country.equals("Italy")) {
                        country1.setText(country);
                    }
                    if (country.equals("USA")) {
                        country2.setText(country);
                    }
                    if (country.equals("Iran")) {
                        country3.setText(country);
                        loading.setVisibility(View.GONE);
                        main.setVisibility(View.VISIBLE);
                        refreshLayout.setRefreshing(false);


                    }

                    newCases.setText(checkCaseEmpty(response.body().getLatest_stat_by_country().get(0).getNew_cases()));
                    activeCases.setText(checkCaseEmpty(response.body().getLatest_stat_by_country().get(0).getActive_cases()));
                    deathCases.setText(checkCaseEmpty(response.body().getLatest_stat_by_country().get(0).getTotal_deaths()));
                }
            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                refreshLayout.setRefreshing(false);
                loading.setVisibility(View.GONE);
                main.setVisibility(View.GONE);
                noInternet.setVisibility(View.VISIBLE);
                //Toast.makeText(AboutCorona.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String checkCaseEmpty(String cases) {
        if (cases.equals("")) {
            return "0";
        }
        return cases;
    }

}
