package com.example.news.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.models.NewsItem;
import com.example.news.repository.NewsItemRepository;

import java.util.List;

public class ArticlesViewModelTBD extends ViewModel {

    private NewsItemRepository newsItemRepository;
    private boolean mIsPerformingQuery;

    public ArticlesViewModelTBD() {
        newsItemRepository = NewsItemRepository.getInstance();
        mIsPerformingQuery = false;
    }

    public MutableLiveData<List<NewsItem>> getArticlesObserver() {
        return newsItemRepository.getArticlesObserver();
    }

    public void getArticles(String keyword, int pageNumber) {
        mIsPerformingQuery = true;
        newsItemRepository.getArticles(keyword, pageNumber);
    }

    public void searchNextPage() {
        if (!mIsPerformingQuery) {
            newsItemRepository.searchNextPageForArticles();
        }
    }

    public void setIsPerformingQuery(Boolean isPerformingQuery) {
        mIsPerformingQuery = isPerformingQuery;
    }
}
