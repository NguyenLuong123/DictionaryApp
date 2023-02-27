package com.example.myapplication.model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Word {
    @SerializedName("word")
    private String word;
    @SerializedName("phonetics")
    private ArrayList<Phonetic> phonetics;
    @SerializedName("meanings")
    private ArrayList<Meaning> meanings;
    private String phonetic_text;
    private String phonetic_sound;
    private String mean;

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getMean() {
        return mean;
    }

    public String getPhonetic_sound() {
        return phonetic_sound;
    }

    public String getPhonetic_text() {
        return phonetic_text;
    }

    public void setPhonetic_sound(String phonetic_sound) {
        this.phonetic_sound = phonetic_sound;
    }

    public void setPhonetic_text(String phonetic_text) {
        this.phonetic_text = phonetic_text;
    }

    public ArrayList<Meaning> getMeanings() {
        return meanings;
    }
    public String getWord() {
        return word;
    }
    public ArrayList<Phonetic> getPhonetics() {
        return phonetics;
    }

    public void setMeanings(ArrayList<Meaning> meanings) {
        this.meanings = meanings;
    }


    public void setWord(String word) {
        this.word = word;
    }
    public Word(String word, String phonetic_text, String phonetic_sound, String mean){
        this.word = word;
        this.phonetic_sound = phonetic_sound;
        this.phonetic_text = phonetic_text;
        this.mean = mean;
    }
    public Boolean isSaved = false;
    public Word(){}
}
