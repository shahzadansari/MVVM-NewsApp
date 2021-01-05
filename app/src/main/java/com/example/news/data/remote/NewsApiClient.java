package com.example.news.data.remote;

import androidx.lifecycle.MutableLiveData;

import com.example.news.models.NewsItem;
import com.example.news.models.RootJsonData;
import com.example.news.utils.Utils;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsApiClient {

    private MutableLiveData<List<NewsItem>> newsItems;
    private MutableLiveData<List<NewsItem>> articles;
    private MutableLiveData<List<NewsItem>> headlines;
    public static final String SORT_ORDER = "publishedAt";

    private static NewsApiClient instance;

    public NewsApiClient() {
        newsItems = new MutableLiveData<>();
        articles = new MutableLiveData<>();
        headlines = new MutableLiveData<>();
    }

    public static NewsApiClient getInstance() {
        if (instance == null) {
            instance = new NewsApiClient();
        }
        return instance;
    }

    public MutableLiveData<List<NewsItem>> getNewsObserver() {
        return newsItems;
    }

    public MutableLiveData<List<NewsItem>> getArticlesObserver() {
        return articles;
    }

    public MutableLiveData<List<NewsItem>> getHeadlinesObserver() {
        return headlines;
    }

    public void getNews(String keyword, String apiKey) {

        Call<RootJsonData> rootJsonDataCall = createNewsJsonDataCall(keyword, apiKey);
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

    public void getArticles(String keyword, int pageNumber) {

        Call<RootJsonData> rootJsonDataCall = createArticlesJsonDataCall(keyword, pageNumber);
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

    public void getHeadlines(String keyword, String apiKey) {

        Call<RootJsonData> rootJsonDataCall = createHeadlinesJsonDataCall(keyword, apiKey);
        rootJsonDataCall.enqueue(new Callback<RootJsonData>() {
            @Override
            public void onResponse(Call<RootJsonData> call, Response<RootJsonData> response) {
                headlines.postValue(response.body().getNewsItems());
            }

            @Override
            public void onFailure(Call<RootJsonData> call, Throwable t) {
                headlines.postValue(null);
            }
        });
    }

    private Call<RootJsonData> createNewsJsonDataCall(String keyword, String apiKey) {

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


    private Call<RootJsonData> createArticlesJsonDataCall(String keyword, int pageNumber) {
        String language = Locale.getDefault().getLanguage();
        boolean isLanguageAvailable = Utils.checkLanguage(language);
        if (isLanguageAvailable) {
            language = Utils.getLanguage();
        } else {
            language = "en";
        }

        NewsAPI newsAPI = ServiceGenerator.createService(NewsAPI.class);
        return newsAPI.searchArticlesByKeyWord(keyword, SORT_ORDER, language, Utils.API_KEY, pageNumber);
    }


    private Call<RootJsonData> createHeadlinesJsonDataCall(String category, String apiKey) {
        String language = Locale.getDefault().getLanguage();
        boolean isLanguageAvailable = Utils.checkLanguage(language);
        if (isLanguageAvailable) {
            language = Utils.getLanguage();
        } else {
            language = "en";
        }

        NewsAPI newsAPI = ServiceGenerator.createService(NewsAPI.class);

        return newsAPI.getTopHeadlinesByCategory(category, language, apiKey);
    }
}