package com.example.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.example.news.models.NewsItem;

public class ArticlesViewModel extends ViewModel {

    public LiveData itemPagedList;
    LiveData<PageKeyedDataSource<Integer, NewsItem>> liveDataSource;

    public ArticlesViewModel() {

        ArticlesDataSourceFactory itemDataSourceFactory = new ArticlesDataSourceFactory();
        liveDataSource = itemDataSourceFactory.getArticlesLiveDataSource();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(10).build();

        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, pagedListConfig)).build();
    }
}
