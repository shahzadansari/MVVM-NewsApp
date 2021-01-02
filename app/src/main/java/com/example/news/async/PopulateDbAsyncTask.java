package com.example.news.async;

import android.os.AsyncTask;

import com.example.news.models.NewsItem;
import com.example.news.models.Source;
import com.example.news.data.persistence.ArticleDao;
import com.example.news.data.persistence.NewsRoomDatabase;


public class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

    private final ArticleDao mDao;

    public PopulateDbAsyncTask(NewsRoomDatabase db) {
        mDao = db.articleDao();
    }

    @Override
    protected Void doInBackground(final Void... params) {
        NewsItem newsItem = new NewsItem();
        newsItem.setAuthor("AuthorName");
        newsItem.setTitle("NewsTitle");
        newsItem.setUrl("imageUrl");
        newsItem.setSource(new Source("BBC"));
        newsItem.setDescription("Description");
        newsItem.setPublishedAt("2 Jan 2020");

        mDao.insert(newsItem);
        return null;
    }
}