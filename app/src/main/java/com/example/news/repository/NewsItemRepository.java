package com.example.news.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.news.async.DeleteAllNotesAsyncTask;
import com.example.news.async.DeleteNoteAsyncTask;
import com.example.news.async.InsertNoteAsyncTask;
import com.example.news.data.local.ArticleDao;
import com.example.news.data.local.NewsRoomDatabase;
import com.example.news.data.remote.NewsApiClient;
import com.example.news.models.NewsItem;

import java.util.List;

public class NewsItemRepository {

    private ArticleDao mArticleDao;
    private LiveData<List<NewsItem>> mAllSavedArticles;

    private static NewsItemRepository instance;
    private NewsApiClient mNewsApiClient;

    private String mQuery;
    private int mPageNumber;

    public static NewsItemRepository getInstance() {
        if (instance == null) {
            instance = new NewsItemRepository();
        }
        return instance;
    }

    public NewsItemRepository() {
        mNewsApiClient = NewsApiClient.getInstance();
    }

    public NewsItemRepository(Application application) {
        NewsRoomDatabase db = NewsRoomDatabase.getDatabase(application);
        mArticleDao = db.articleDao();
        mAllSavedArticles = mArticleDao.getAllSavedArticles();
    }

    public MutableLiveData<List<NewsItem>> getNewsObserver() {
        return mNewsApiClient.getNewsObserver();
    }

    public MutableLiveData<List<NewsItem>> getArticlesObserver() {
        return mNewsApiClient.getArticlesObserver();
    }

    public MutableLiveData<List<NewsItem>> getHeadlinesObserver() {
        return mNewsApiClient.getHeadlinesObserver();
    }

    public LiveData<List<NewsItem>> getAllSavedArticles() {
        return mAllSavedArticles;
    }

    public void insert(NewsItem newsItem) {
        new InsertNoteAsyncTask(mArticleDao).execute(newsItem);
    }

    public void delete(NewsItem newsItem) {
        new DeleteNoteAsyncTask(mArticleDao).execute(newsItem);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(mArticleDao).execute();
    }

    public void getHeadlines(String keyword, int pageNumber) {
        mNewsApiClient.getHeadlines(keyword, pageNumber);
    }

    public void getArticles(String keyword, int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        mQuery = keyword;
        mPageNumber = pageNumber;
        mNewsApiClient.getArticles(keyword, pageNumber);
    }

    public void searchNextPageForArticles() {
        mNewsApiClient.getArticles(mQuery, mPageNumber + 1);
    }

    public void getNews(String keyword, int pageNumber) {
        mNewsApiClient.getNews(keyword, pageNumber);
    }
}
