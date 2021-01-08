package com.example.news.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.news.pagination.news.NewsDataSource;
import com.example.news.pagination.news.NewsDataSourceFactory;
import com.example.news.models.NewsItem;
import com.example.news.utils.DataStatus;

public class NewsViewModel extends ViewModel {

    public LiveData<PagedList<NewsItem>> itemPagedList;
    private MutableLiveData<NewsDataSource> liveDataSource;
    private NewsDataSourceFactory newsDataSourceFactory;
    private LiveData dataStatus;

    public NewsViewModel() {

        newsDataSourceFactory = new NewsDataSourceFactory();
        liveDataSource = newsDataSourceFactory.getNewsLiveDataSource();
        dataStatus = newsDataSourceFactory.getDataStatusLiveData();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(10).build();

        itemPagedList = (new LivePagedListBuilder(newsDataSourceFactory, pagedListConfig)).build();
    }

    public void setKeyword(String query) {
        newsDataSourceFactory.setQuery(query);
        refreshData();
    }

    void refreshData() {
        if (itemPagedList.getValue() != null) {
            itemPagedList.getValue().getDataSource().invalidate();
        }
    }

    public LiveData<DataStatus> getDataStatus() {
        return dataStatus;
    }
}
