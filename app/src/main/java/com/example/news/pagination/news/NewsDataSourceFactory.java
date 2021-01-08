package com.example.news.pagination.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;

import com.example.news.models.NewsItem;
import com.example.news.utils.DataStatus;

public class NewsDataSourceFactory extends DataSource.Factory {

    private final MutableLiveData<NewsDataSource> itemLiveDataSource = new MutableLiveData<>();
    private String mQuery;
    private final LiveData<DataStatus> dataStatusLiveData = Transformations
            .switchMap(itemLiveDataSource, NewsDataSource::getDataStatusMutableLiveData);

    public NewsDataSourceFactory() {
        mQuery = "";
    }

    @Override
    public DataSource<Integer, NewsItem> create() {
        NewsDataSource itemDataSource = new NewsDataSource(mQuery);
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<NewsDataSource> getNewsLiveDataSource() {
        return itemLiveDataSource;
    }

    public void setQuery(String query) {
        mQuery = query;
    }

    public LiveData<DataStatus> getDataStatusLiveData() {
        return dataStatusLiveData;
    }
}