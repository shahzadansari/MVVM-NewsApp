package com.example.news;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.news.models.NewsItem;

public class NewsItemDataSourceFactory extends DataSource.Factory {

    //creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, NewsItem>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, NewsItem> create() {
        //getting our data source object
        NewsItemDataSource itemDataSource = new NewsItemDataSource();

        //posting the data source to get the values
        itemLiveDataSource.postValue(itemDataSource);

        //returning the data source
        return itemDataSource;
    }


    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, NewsItem>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}