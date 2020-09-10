package com.example.news;

import com.example.news.models.RootJsonData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsAPI {

    @GET("top-headlines?country=us&apiKey=c2194f57d73e4392ae4ee0bf69e9d391")
    Call<RootJsonData> getRootJsonData();
}
