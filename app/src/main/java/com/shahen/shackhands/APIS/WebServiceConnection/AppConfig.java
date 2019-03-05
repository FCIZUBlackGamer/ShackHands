package com.shahen.shackhands.APIS.WebServiceConnection;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppConfig {
    private String BASE_URL;
//    private static String BASE_URL = "http://www.teamupapi.rivile.com/";
    public AppConfig(String url){
        BASE_URL = url;
    }
    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
