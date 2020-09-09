package com.example.news.JSONResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RootJsonData {

    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("articles")
    private List<NewsItem> articles = null;

    public List<NewsItem> getArticles() {
        return articles;
    }
}
