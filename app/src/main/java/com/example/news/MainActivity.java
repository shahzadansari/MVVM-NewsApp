package com.example.news;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsItem.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    private RecyclerView recyclerView;
    private NewsItemAdapter adapter;
    private List<NewsItem> newsItemList = new ArrayList<>();
    public static final int NEWS_LOADER_ID = 6;
    public static final String REQUEST_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=c2194f57d73e4392ae4ee0bf69e9d391";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);

//        adapter = new NewsItemAdapter(this, new ArrayList<NewsItem>());
//
//        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(NEWS_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int i, Bundle bundle) {
        Loader newsLoader = new NewsLoader(this, REQUEST_URL);
        return newsLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> newsItems) {

        if (newsItems != null && !newsItems.isEmpty()) {
            adapter = new NewsItemAdapter(this, newsItems);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
    }
}