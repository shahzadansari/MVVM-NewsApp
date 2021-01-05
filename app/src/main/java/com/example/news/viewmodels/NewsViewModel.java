package com.example.news.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.models.NewsItem;
import com.example.news.repository.NewsItemRepository;

import java.util.List;

public class NewsViewModel extends ViewModel {

    private NewsItemRepository newsItemRepository;

    public NewsViewModel() {
        newsItemRepository = NewsItemRepository.getInstance();
    }

    public MutableLiveData<List<NewsItem>> getNewsItemListObserver() {
        return newsItemRepository.getNewsObserver();
    }

    public void getNews(String keyword, int pageNumber) {
        newsItemRepository.getNews(keyword, pageNumber);
    }
}
