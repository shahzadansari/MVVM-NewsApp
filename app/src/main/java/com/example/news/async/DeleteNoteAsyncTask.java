package com.example.news.async;

import android.os.AsyncTask;

import com.example.news.models.NewsItem;
import com.example.news.data.persistence.ArticleDao;

public class DeleteNoteAsyncTask extends AsyncTask<NewsItem, Void, Void> {

    private ArticleDao myAsyncTaskDao;

    public DeleteNoteAsyncTask(ArticleDao noteDao) {
        myAsyncTaskDao = noteDao;
    }

    @Override
    protected Void doInBackground(final NewsItem... newsItems) {
        myAsyncTaskDao.deleteArticle(newsItems[0]);
        return null;
    }
}