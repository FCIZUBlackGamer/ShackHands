package com.shahen.shackhands.APIS.WebServiceConnection;

import com.shahen.shackhands.APIS.API;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiConfig {

    @GET(API.MENU)
    Call<ResponseModel> loadMenu();

    @GET(API.PASSDATE)
    Call<String> getPass();

}

