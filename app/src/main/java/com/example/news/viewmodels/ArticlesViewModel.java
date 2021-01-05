package com.example.news.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.models.NewsItem;
import com.example.news.repository.NewsItemRepository;

import java.util.List;

public class ArticlesViewModel extends ViewModel {

    private NewsItemRepository newsItemRepository;

    public ArticlesViewModel() {
        newsItemRepository = NewsItemRepository.getInstance();
    }

    public MutableLiveData<List<NewsItem>> getArticlesObserver() {
        return newsItemRepository.getArticlesObserver();
    }

    public void getArticles(String keyword, int pageNumber) {
        newsItemRepository.getArticles(keyword, pageNumber);
    }
}
