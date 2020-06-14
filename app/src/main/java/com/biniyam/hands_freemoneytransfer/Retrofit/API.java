package com.biniyam.hands_freemoneytransfer.Retrofit;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface API {


    @GET("/coronavirus/latest_stat_by_country.php")
    Call<Country> getCoronaByCountry(@Query("country") String country, @Header("x-rapidapi-key") String token, @Header("x-rapidapi-host") String host );


}
