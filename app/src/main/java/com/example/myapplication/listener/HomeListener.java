package com.example.myapplication.listener;

import com.example.myapplication.model.Word;

public interface HomeListener {
    void onUpdateFromHome(Word word);
    void onUpdateFromSearch(String text);
}
