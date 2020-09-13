package com.example.news.models;

import com.google.gson.annotations.SerializedName;

public class Source {

    public Source(String id, String name) {
        this.id = id;
        this.name = name;
    }

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
