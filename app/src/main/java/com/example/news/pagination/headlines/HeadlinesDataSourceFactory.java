package com.example.news.pagination.headlines;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;

import com.example.news.models.NewsItem;
import com.example.news.utils.DataStatus;

public class HeadlinesDataSourceFactory extends DataSource.Factory {

    private final MutableLiveData<HeadlinesDataSource> itemLiveDataSource = new MutableLiveData<>();
    private String mCategory;
    private final LiveData<DataStatus> dataStatusLiveData = Transformations
            .switchMap(itemLiveDataSource, HeadlinesDataSource::getDataStatusMutableLiveData);

    public HeadlinesDataSourceFactory() {
        mCategory = "business";
    }

    @Override
    public DataSource<Integer, NewsItem> create() {
        HeadlinesDataSource itemDataSource = new HeadlinesDataSource(mCategory);
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<HeadlinesDataSource> getNewsLiveDataSource() {
        return itemLiveDataSource;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public LiveData<DataStatus> getDataStatusLiveData() {
        return dataStatusLiveData;
    }
}