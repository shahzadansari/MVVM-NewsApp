package com.example.news.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.news.models.NewsItem;
import com.example.news.pagination.headlines.HeadlinesDataSource;
import com.example.news.pagination.headlines.HeadlinesDataSourceFactory;
import com.example.news.utils.DataStatus;

public class HeadlinesViewModel extends ViewModel {

    public LiveData<PagedList<NewsItem>> itemPagedList;
    private MutableLiveData<HeadlinesDataSource> liveDataSource;
    private HeadlinesDataSourceFactory newsDataSourceFactory;
    private LiveData dataStatus;

    public HeadlinesViewModel() {

        newsDataSourceFactory = new HeadlinesDataSourceFactory();
        liveDataSource = newsDataSourceFactory.getNewsLiveDataSource();
        dataStatus = newsDataSourceFactory.getDataStatusLiveData();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(10).build();

        itemPagedList = (new LivePagedListBuilder(newsDataSourceFactory, pagedListConfig)).build();
    }

    public void setCategory(String category) {
        newsDataSourceFactory.setCategory(category);
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
