package com.example.news.pagination.articles;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.news.data.remote.NewsAPI;
import com.example.news.data.remote.ServiceGenerator;
import com.example.news.models.NewsItem;
import com.example.news.models.RootJsonData;
import com.example.news.utils.DataStatus;
import com.example.news.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticlesDataSource extends PageKeyedDataSource<Integer, NewsItem> {

    private static final int FIRST_PAGE = 1;
    public static final String SORT_ORDER = "publishedAt";
    public static final String LANGUAGE = "en";
    public static final String API_KEY = Utils.API_KEY;
    public static final int PAGE_SIZE = 10;

    private String mKeyword;
    private MutableLiveData<DataStatus> dataStatusMutableLiveData;

    public ArticlesDataSource(String keyword) {
        mKeyword = keyword;
        dataStatusMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataStatus> getDataStatusMutableLiveData() {
        return dataStatusMutableLiveData;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, NewsItem> callback) {
        dataStatusMutableLiveData.postValue(DataStatus.LOADING);
        NewsAPI newsAPI = ServiceGenerator.createService(NewsAPI.class);
        Call<RootJsonData> call = newsAPI.searchArticlesByKeyWord(mKeyword, SORT_ORDER, LANGUAGE, API_KEY, FIRST_PAGE, PAGE_SIZE);
        call.enqueue(new Callback<RootJsonData>() {
            @Override
            public void onResponse(Call<RootJsonData> call, Response<RootJsonData> response) {
                if (response.body() != null) {
                    callback.onResult(response.body().getNewsItems(), null, FIRST_PAGE + 1);
                    dataStatusMutableLiveData.postValue(DataStatus.LOADED);
                }
                if(response.body().getTotalResults() == 0){
                    dataStatusMutableLiveData.postValue(DataStatus.EMPTY);
                }
            }

            @Override
            public void onFailure(Call<RootJsonData> call, Throwable t) {
                dataStatusMutableLiveData.postValue(DataStatus.ERROR);
            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, NewsItem> callback) {
        NewsAPI newsAPI = ServiceGenerator.createService(NewsAPI.class);
        Call<RootJsonData> call = newsAPI.searchArticlesByKeyWord(mKeyword, SORT_ORDER, LANGUAGE, API_KEY, FIRST_PAGE, PAGE_SIZE);
        call.enqueue(new Callback<RootJsonData>() {
            @Override
            public void onResponse(Call<RootJsonData> call, Response<RootJsonData> response) {
                // if the current page is greater than one
                // we are decrementing the page number
                // else there is no previous page
                Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                if (response.body() != null) {
                    // passing the loaded data
                    // and the previous page key
                    callback.onResult(response.body().getNewsItems(), adjacentKey);
                }
            }

            @Override
            public void onFailure(Call<RootJsonData> call, Throwable t) {
            }
        });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, NewsItem> callback) {
        NewsAPI newsAPI = ServiceGenerator.createService(NewsAPI.class);
        Call<RootJsonData> call = newsAPI.searchArticlesByKeyWord(mKeyword, SORT_ORDER, LANGUAGE, API_KEY, params.key, PAGE_SIZE);
        call.enqueue(new Callback<RootJsonData>() {
            @Override
            public void onResponse(Call<RootJsonData> call, Response<RootJsonData> response) {
                dataStatusMutableLiveData.postValue(DataStatus.LOADED);

                if (response.code() == 429) {
                    // no more results
                    List<NewsItem> emptyList = new ArrayList<>();
                    callback.onResult(emptyList, null);
                }

                if (response.body() != null) {
                    // if the response has next page
                    // incrementing the next page number
                    Integer key = params.key + 1;

                    // passing the loaded data and next page value
                    if (!response.body().getNewsItems().isEmpty()) {
                        callback.onResult(response.body().getNewsItems(), key);
                    }
                }
            }

            @Override
            public void onFailure(Call<RootJsonData> call, Throwable t) {
                dataStatusMutableLiveData.postValue(DataStatus.ERROR);
            }
        });
    }
}
