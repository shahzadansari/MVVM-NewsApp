package com.example.news;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.news.models.NewsItem;

public class ArticlesDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, NewsItem>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, NewsItem> create() {
        ArticlesDataSource itemDataSource = new ArticlesDataSource();
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, NewsItem>> getArticlesLiveDataSource() {
        return itemLiveDataSource;
    }
}