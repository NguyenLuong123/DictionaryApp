package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Word1 {
    @SerializedName("tratu")
    private ArrayList<TraTu> traTu;
    public ArrayList<TraTu> getTraTu() {
        return traTu;
    }
    public void setTraTu(ArrayList<TraTu> traTu) {
        this.traTu = traTu;
    }
}
