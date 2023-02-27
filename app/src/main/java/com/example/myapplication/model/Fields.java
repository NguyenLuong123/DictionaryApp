package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class Fields {
    @SerializedName("fulltext")
    private String meaning;
    public String getMeaning() {
        return meaning;
    }
}
