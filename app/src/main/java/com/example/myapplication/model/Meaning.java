package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Meaning {
    @SerializedName("partOfSpeech")
    private String partOfSpeech;
    @SerializedName("definitions")
    private ArrayList<Definition> definitions;
    @SerializedName("synonyms")
    private ArrayList<String> synonyms;
    @SerializedName("antonyms")
    private ArrayList<String> antonyms;

    public ArrayList<String> getAntonyms() {
        return antonyms;
    }

    public ArrayList<Definition> getDefinitions() {
        return definitions;
    }

    public ArrayList<String> getSynonyms() {
        return synonyms;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setDefinitions(ArrayList<Definition> definitions) {
        this.definitions = definitions;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public void setSynonyms(ArrayList<String> synonyms) {
        this.synonyms = synonyms;
    }
}
