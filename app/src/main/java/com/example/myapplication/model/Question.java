package com.example.myapplication.model;

import java.util.ArrayList;

public class Question {
    private ArrayList<String> Answer;
    private Word content;

    public Question(ArrayList<String> answer, Word content) {
        Answer = answer;
        this.content = content;
    }

    public ArrayList<String> getAnswer() {
        return Answer;
    }

    public void setAnswer(ArrayList<String> answer) {
        Answer = answer;
    }

    public Word getContent() {
        return content;
    }

    public void setContent(Word content) {
        this.content = content;
    }
    public int findCorrect() {
        for (int i = 0; i < 4; i++) {
            if((content.getWord()).equals(Answer.get(i))) {
                return i;
            }
        }
        return 0;

    }

}