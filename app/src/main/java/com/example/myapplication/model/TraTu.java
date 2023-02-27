package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;

public class TraTu {
    @SerializedName("fields")
    private Fields fields;

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }
}
