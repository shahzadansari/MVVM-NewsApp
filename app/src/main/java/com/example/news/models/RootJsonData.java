package com.example.news.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RootJsonData {

    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("articles")
    private List<NewsItem> newsItemList = null;

    public List<NewsItem> getNewsItems() {
        return newsItemList;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }
}
