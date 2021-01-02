package com.example.news.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.news.async.DeleteAllNotesAsyncTask;
import com.example.news.async.DeleteNoteAsyncTask;
import com.example.news.async.InsertNoteAsyncTask;
import com.example.news.data.persistence.ArticleDao;
import com.example.news.data.persistence.NewsRoomDatabase;
import com.example.news.models.NewsItem;

import java.util.List;

public class NewsItemRepository {

    private ArticleDao mArticleDao;
    private LiveData<List<NewsItem>> mAllSavedArticles;

    public NewsItemRepository(Application application) {

        NewsRoomDatabase db = NewsRoomDatabase.getDatabase(application);
        mArticleDao = db.articleDao();
        mAllSavedArticles = mArticleDao.getAllSavedArticles();
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
}
