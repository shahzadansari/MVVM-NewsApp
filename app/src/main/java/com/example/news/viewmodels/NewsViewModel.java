package com.example.news.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.api.NewsAPI;
import com.example.news.models.NewsItem;
import com.example.news.models.RootJsonData;
import com.example.news.utils.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsViewModel extends ViewModel {

    private MutableLiveData<List<NewsItem>> newsItems;

    public NewsViewModel() {
        newsItems = new MutableLiveData<>();
    }

    //    public NewsViewModel(@NonNull Application application) {
//        super(application);
//        newsItems = new MutableLiveData<>();
//    }

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

        NewsAPI newsAPI = ServiceGenerator.createService(NewsAPI.class);

        Call<RootJsonData> rootJsonDataCall;
        rootJsonDataCall = newsAPI.getTopHeadlinesByLanguage("en", apiKey);

//        if (keyword.isEmpty()) {
//
//            if (isLocaleAvailable) {
//                rootJsonDataCall = newsAPI.getTopHeadlinesByCountry(locale, language, getString(R.string.API_KEY));
//            } else {
//                if (isLanguageAvailable) {
//                    language = Utils.getLanguage();
//                } else {
//                    language = "en";
//                }
//                rootJsonDataCall = newsAPI.getTopHeadlinesByLanguage(language, getString(R.string.API_KEY));
//
//            }
//        } else {
//            rootJsonDataCall = newsAPI.searchNewsByKeyWord(keyword, SORT_ORDER, language, getString(R.string.API_KEY));
//        }

        return rootJsonDataCall;
    }
}
