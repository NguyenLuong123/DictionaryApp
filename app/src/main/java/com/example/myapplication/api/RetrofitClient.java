package com.example.myapplication.api;

import com.example.myapplication.Constant.ApiConstant;
import com.example.myapplication.model.Word;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private Retrofit retrofit;
    public Retrofit getRetrofitInstance(String baseURL) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
