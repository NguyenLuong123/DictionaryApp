package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class Definition {
    @SerializedName("definition")
    private String definition;
    @SerializedName("example")
    private String example;

    public String getDefinition() {
        return definition;
    }

    public String getExample() {
        return example;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
