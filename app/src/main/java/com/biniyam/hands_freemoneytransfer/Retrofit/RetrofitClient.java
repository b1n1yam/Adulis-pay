package com.biniyam.hands_freemoneytransfer.Retrofit;


import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.JavaNetCookieJar;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;

    public static Retrofit getInstance(){
        //create OKHTTPCLIENT
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        CookieHandler cookieHandler = new CookieManager();
        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(interceptor)
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        if(instance == null)
            instance= new Retrofit.Builder()
                    .baseUrl("https://coronavirus-monitor.p.rapidapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
///*192.168.44.125*/
        return instance;
    }

}
