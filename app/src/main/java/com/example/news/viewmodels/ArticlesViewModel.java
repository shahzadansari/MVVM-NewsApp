package com.example.news.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.api.NewsAPI;
import com.example.news.models.NewsItem;
import com.example.news.models.RootJsonData;
import com.example.news.utils.ServiceGenerator;
import com.example.news.utils.Utils;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticlesViewModel extends ViewModel {

    public static final String SORT_ORDER = "publishedAt";
    // possible options: publishedAt, popularity, relevancy

    private MutableLiveData<List<NewsItem>> articles;

    public ArticlesViewModel() {
        articles = new MutableLiveData<>();
    }

    public MutableLiveData<List<NewsItem>> getArticlesObserver() {
        return articles;
    }

    public void fetchData(String keyword, String apiKey) {

        Call<RootJsonData> rootJsonDataCall = createJsonDataCall(keyword, apiKey);
        rootJsonDataCall.enqueue(new Callback<RootJsonData>() {
            @Override
            public void onResponse(Call<RootJsonData> call, Response<RootJsonData> response) {
                articles.postValue(response.body().getNewsItems());
            }

            @Override
            public void onFailure(Call<RootJsonData> call, Throwable t) {
                articles.postValue(null);
            }
        });
    }

    private Call<RootJsonData> createJsonDataCall(String keyword, String apiKey) {
        String language = Locale.getDefault().getLanguage();
        boolean isLanguageAvailable = Utils.checkLanguage(language);
        if (isLanguageAvailable) {
            language = Utils.getLanguage();
        } else {
            language = "en";
        }

        NewsAPI newsAPI = ServiceGenerator.createService(NewsAPI.class);

        return newsAPI.searchArticlesByKeyWord(keyword, SORT_ORDER, language, apiKey);
    }
}
