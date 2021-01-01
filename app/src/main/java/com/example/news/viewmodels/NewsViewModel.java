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

public class NewsViewModel extends ViewModel {

    private MutableLiveData<List<NewsItem>> newsItems;
    public static final String SORT_ORDER = "publishedAt";

    public NewsViewModel() {
        newsItems = new MutableLiveData<>();
    }

    public MutableLiveData<List<NewsItem>> getNewsItemListObserver() {
        return newsItems;
    }

    public void fetchData(String keyword, String apiKey) {

        Call<RootJsonData> rootJsonDataCall = createJsonDataCall(keyword, apiKey);
        rootJsonDataCall.enqueue(new Callback<RootJsonData>() {
            @Override
            public void onResponse(Call<RootJsonData> call, Response<RootJsonData> response) {
                newsItems.postValue(response.body().getNewsItems());
            }

            @Override
            public void onFailure(Call<RootJsonData> call, Throwable t) {
                newsItems.postValue(null);
            }
        });
    }

    private Call<RootJsonData> createJsonDataCall(String keyword, String apiKey) {

        String locale = Utils.getCountry();
        boolean isLocaleAvailable = Utils.checkLocale(locale);

        String language = Locale.getDefault().getLanguage();
        boolean isLanguageAvailable = Utils.checkLanguage(language);

        NewsAPI newsAPI = ServiceGenerator.createService(NewsAPI.class);
        Call<RootJsonData> rootJsonDataCall;

        if (keyword.isEmpty()) {

            if (isLocaleAvailable) {
                rootJsonDataCall = newsAPI.getTopHeadlinesByCountry(locale, language, apiKey);
            } else {
                if (isLanguageAvailable) {
                    language = Utils.getLanguage();
                } else {
                    language = "en";
                }

                rootJsonDataCall = newsAPI.getTopHeadlinesByLanguage(language, apiKey);
            }
        } else {
            rootJsonDataCall = newsAPI.searchNewsByKeyWord(keyword, SORT_ORDER, language, apiKey);
        }

        return rootJsonDataCall;
    }
}
