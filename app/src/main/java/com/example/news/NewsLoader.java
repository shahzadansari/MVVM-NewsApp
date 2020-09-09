package com.example.news;

import android.content.AsyncTaskLoader;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {

    private String mUrl;

    public NewsLoader(@NonNull Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public List<NewsItem> loadInBackground() {
        List<NewsItem> newsItemList = QueryUtils.fetchNewsData(mUrl);
        return newsItemList;
    }
}
