package com.example.news.async;

import android.os.AsyncTask;

import com.example.news.persistence.ArticleDao;

public class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {

    private ArticleDao mAsyncTaskDao;

    public DeleteAllNotesAsyncTask(ArticleDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        mAsyncTaskDao.deleteAllSavedArticles();
        return null;
    }
}
