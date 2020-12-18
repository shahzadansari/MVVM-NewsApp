package com.example.news.api;

import com.example.news.models.RootJsonData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsAPI {

    @GET("top-headlines?country=us&apiKey=c2194f57d73e4392ae4ee0bf69e9d391")
    Call<RootJsonData> getTopHeadlines();

    @GET("everything")
    Call<RootJsonData> searchArticlesWithKeyWord(@Query("q") String keyword,
                                                 @Query("sortBy") String sortBy,
                                                 @Query("language") String language,
                                                 @Query("apiKey") String apiKey);
}
