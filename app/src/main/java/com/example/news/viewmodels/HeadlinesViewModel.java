package com.example.news.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.models.NewsItem;
import com.example.news.repository.NewsItemRepository;

import java.util.List;

public class HeadlinesViewModel extends ViewModel {

    private NewsItemRepository newsItemRepository;

    public HeadlinesViewModel() {
        newsItemRepository = NewsItemRepository.getInstance();
    }

    public MutableLiveData<List<NewsItem>> getHeadlinesObserver() {
        return newsItemRepository.getHeadlinesObserver();
    }

    public void getHeadlines(String keyword, int pageNumber){
        newsItemRepository.getHeadlines(keyword, pageNumber);
    }
}
