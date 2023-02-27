package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class Phonetic {
    @SerializedName("audio")
    private String audio;
    @SerializedName("text")
    private String text;

    public String getText() {
        return text;
    }

    public String getAudio() {
        return audio;
    }
}
