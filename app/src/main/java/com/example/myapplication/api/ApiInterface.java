package com.example.myapplication.api;

import com.example.myapplication.model.Word;
import com.example.myapplication.model.Word1;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("api/v2/entries/en/{word}")
    Call<ArrayList<Word>>getWords(@Path("word") String word);
    @GET("s/{value}/{lang}")
    Call<Word1>getWord(@Path("value") String word, @Path("lang") String lang);
    @GET("{lang}/{value}")
    Call<Word1>getWordJF(@Path("value") String word, @Path("lang") String lang);
}