package com.example.news.models;

import com.google.gson.annotations.SerializedName;

public class Source {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
