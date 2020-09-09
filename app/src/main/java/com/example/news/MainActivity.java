package com.example.news;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.JSONResponse.NewsItem;
import com.example.newsItem.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    private RecyclerView recyclerView;
    private NewsItemAdapter adapter;
    public static final int NEWS_LOADER_ID = 6;
    public static final String REQUEST_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=c2194f57d73e4392ae4ee0bf69e9d391";
    private ProgressBar progressBar;
    private TextView emptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_circular);
        emptyStateTextView = findViewById(R.id.empty_view);

        recyclerView = findViewById(R.id.recycler_view);

        adapter = new NewsItemAdapter(this, new ArrayList<NewsItem>());

        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        } else {
            progressBar.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int i, Bundle bundle) {
        Loader newsLoader = new NewsLoader(this, REQUEST_URL);
        return newsLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> newsItems) {

        progressBar.setVisibility(View.GONE);
        if(newsItems == null){
            emptyStateTextView.setText(R.string.no_news_found);
        }

        if (newsItems != null && !newsItems.isEmpty()) {
            adapter = new NewsItemAdapter(this, newsItems);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        adapter = null;
        recyclerView.removeAllViews();
    }
}