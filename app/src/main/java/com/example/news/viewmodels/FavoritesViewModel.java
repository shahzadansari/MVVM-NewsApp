package com.example.news.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.news.models.NewsItem;
import com.example.news.repository.NewsItemRepository;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private NewsItemRepository mRepository;
    private LiveData<List<NewsItem>> mAllSavedArticles;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NewsItemRepository(application);
        mAllSavedArticles = mRepository.getAllSavedArticles();
    }

    public LiveData<List<NewsItem>> getAllSavedArticles() {
        return mAllSavedArticles;
    }

    public void insertArticle(NewsItem newsItem) {
        mRepository.insert(newsItem);
    }

    public void deleteArticle(NewsItem newsItem) {
        mRepository.delete(newsItem);
    }

    public void deleteAllSavedArticles() {
        mRepository.deleteAllNotes();
    }
}
