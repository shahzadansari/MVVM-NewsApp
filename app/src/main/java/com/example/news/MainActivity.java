package com.example.news;

import android.app.SearchManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.adapters.NewsItemAdapter;
import com.example.news.api.NewsAPI;
import com.example.news.models.NewsItem;
import com.example.news.models.RootJsonData;
import com.example.newsItem.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsItemAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyStateTextView;
    private TextView textViewTitle;
    private Context mContext;
    private String keyword = "";
    private SwipeRefreshLayout swipeRefreshLayout;
    public static final String SORT_ORDER = "publishedAt";
    private String language = "";
    private String locale = "";

    private boolean isLanguageAvailable = false;
    private boolean isLocaleAvailable = false;
    private BottomNavigationView bottomNavigationView;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "onNavigationItemSelected: " + item.getItemId());
                return true;
            }

        });

        locale = Utils.getCountry();
        isLocaleAvailable = Utils.checkLocale(locale);

        language = Locale.getDefault().getLanguage();
        isLanguageAvailable = Utils.checkLanguage(language);

        mContext = this;
        progressBar = findViewById(R.id.progress_circular);
        emptyStateTextView = findViewById(R.id.empty_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        textViewTitle = findViewById(R.id.text_view_top_headlines);

        if (savedInstanceState != null) {
            keyword = savedInstanceState.getString("keyword");
        }

        initEmptyRecyclerView();
        fetchData(keyword);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchData(keyword));
    }

    public void initEmptyRecyclerView() {

        recyclerView = findViewById(R.id.recycler_view);
        adapter = new NewsItemAdapter(mContext, new ArrayList<NewsItem>(), this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void fetchData(String keyword) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            Call<RootJsonData> rootJsonDataCall = createJsonDataCall(keyword);
            rootJsonDataCall.enqueue(new Callback<RootJsonData>() {
                @Override
                public void onResponse(Call<RootJsonData> call, Response<RootJsonData> response) {
                    textViewTitle.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    initRecyclerViewWithResponseData(response);
                }

                @Override
                public void onFailure(Call<RootJsonData> call, Throwable t) {
                    textViewTitle.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    emptyStateTextView.setText(t.getMessage());
                }
            });

        } else {
            progressBar.setVisibility(View.GONE);
            textViewTitle.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    public Call<RootJsonData> createJsonDataCall(String keyword) {

        NewsAPI newsAPI = ServiceGenerator.createService(NewsAPI.class);

        Call<RootJsonData> rootJsonDataCall;

        if (keyword.isEmpty()) {

            if (isLocaleAvailable) {
                rootJsonDataCall = newsAPI.getTopHeadlinesByCountry(locale, language, getString(R.string.API_KEY));
            } else {
                if (isLanguageAvailable) {
                    language = Utils.getLanguage();
                } else {
                    language = "en";
                }
                rootJsonDataCall = newsAPI.getTopHeadlinesByLanguage(language, getString(R.string.API_KEY));

            }
        } else {
            rootJsonDataCall = newsAPI.searchArticlesByKeyWord(keyword, SORT_ORDER, language, getString(R.string.API_KEY));
        }

        return rootJsonDataCall;
    }

    public void initRecyclerViewWithResponseData(Response<RootJsonData> response) {

        RootJsonData rootJsonData = response.body();
        List<NewsItem> newsItemList = rootJsonData.getNewsItems();

        progressBar.setVisibility(View.GONE);
        if (newsItemList.isEmpty()) {
            emptyStateTextView.setText(R.string.no_news_found);
        }

        if (!newsItemList.isEmpty()) {
            adapter = new NewsItemAdapter(mContext, newsItemList, this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public void searchKeyword(String query) {
        initEmptyRecyclerView();
        progressBar.setVisibility(View.VISIBLE);
        keyword = query;
        fetchData(keyword);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("keyword", keyword);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        searchKeywordFromSearchView(menu);

        return true;
    }

    private void searchKeywordFromSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Latest News...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    searchKeyword(query);
                } else {
                    Toast.makeText(MainActivity.this, "Type more than two letters!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                keyword = "";
                return true;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);
    }
}