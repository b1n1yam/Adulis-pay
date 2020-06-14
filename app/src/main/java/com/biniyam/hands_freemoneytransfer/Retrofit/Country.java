package com.biniyam.hands_freemoneytransfer.Retrofit;

import java.util.List;

public class Country
{
    public String country ;
    public List<LatestStatByCountry> latest_stat_by_country;

    public Country(String country, List<LatestStatByCountry> latest_stat_by_country) {
        this.country = country;
        this.latest_stat_by_country = latest_stat_by_country;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<LatestStatByCountry> getLatest_stat_by_country() {
        return latest_stat_by_country;
    }

    public void setLatest_stat_by_country(List<LatestStatByCountry> latest_stat_by_country) {
        this.latest_stat_by_country = latest_stat_by_country;
    }
}