package com.example.myapplication;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

interface CatFactApi {
    @GET("api/get")
    Call<List<CatFact>> getFacts();
}
