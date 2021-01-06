package com.example.news;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.news.models.NewsItem;
import com.example.news.utils.DataStatus;

public class ArticlesDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<ArticlesDataSource> itemLiveDataSource;
    private String mQuery;
    private MutableLiveData<DataStatus> dataStatusMutableLiveData = new MutableLiveData<>();

    public ArticlesDataSourceFactory() {
        mQuery = "news";
        itemLiveDataSource = new MutableLiveData<>();
    }

    @Override
    public DataSource<Integer, NewsItem> create() {
        ArticlesDataSource itemDataSource = new ArticlesDataSource(mQuery);
        itemLiveDataSource.postValue(itemDataSource);
        dataStatusMutableLiveData = itemDataSource.getDataStatusMutableLiveData();
        return itemDataSource;
    }

    public MutableLiveData<ArticlesDataSource> getArticlesLiveDataSource() {
        return itemLiveDataSource;
    }

    public void setQuery(String query) {
        mQuery = query;
    }

    public MutableLiveData<DataStatus> getDataStatusMutableLiveData() {
        return dataStatusMutableLiveData;
    }

    public void setDataStatusMutableLiveData(DataStatus dataStatus){
        dataStatusMutableLiveData.postValue(dataStatus);
    }
}