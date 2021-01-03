package com.example.news.async;

import android.os.AsyncTask;

import com.example.news.models.NewsItem;
import com.example.news.data.local.ArticleDao;

public class InsertNoteAsyncTask extends AsyncTask<NewsItem, Void, Void> {

    private ArticleDao mAsyncTaskDao;

    public InsertNoteAsyncTask(ArticleDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(NewsItem... newsItems) {
        mAsyncTaskDao.insert(newsItems[0]);
        return null;
    }
}
