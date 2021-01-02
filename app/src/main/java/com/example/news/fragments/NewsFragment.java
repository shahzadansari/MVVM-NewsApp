package com.example.news.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.adapters.NewsItemAdapterV2;
import com.example.news.models.NewsItem;
import com.example.news.viewmodels.NewsViewModel;
import com.example.newsItem.R;

import java.util.List;

public class NewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewsItemAdapterV2 adapter;

    private ProgressBar progressBar;
    private TextView emptyStateTextView;
    private TextView textViewTitle;

    private Context mContext;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String keyword = "";

    private NewsViewModel mNewsViewModel;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("News");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        mContext = getActivity();
        progressBar = rootView.findViewById(R.id.progress_circular);
        emptyStateTextView = rootView.findViewById(R.id.empty_view);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        textViewTitle = rootView.findViewById(R.id.text_view_top_headlines);
        recyclerView = rootView.findViewById(R.id.recycler_view);

        adapter = new NewsItemAdapterV2(mContext);

        if (savedInstanceState != null) {
            keyword = savedInstanceState.getString("keyword");
        }

        initEmptyRecyclerView();

        mNewsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        subscribeObservers();

        fetchData();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            initEmptyRecyclerView();
            mNewsViewModel.fetchData(keyword, getString(R.string.API_KEY_2));
        });

        setHasOptionsMenu(true);
        return rootView;
    }

    private void fetchData() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mNewsViewModel.fetchData(keyword, getString(R.string.API_KEY_2));
        } else {
            progressBar.setVisibility(View.GONE);
            textViewTitle.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    private void subscribeObservers() {
        mNewsViewModel.getNewsItemListObserver().observe(getViewLifecycleOwner(), new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(List<NewsItem> newsItems) {
                progressBar.setVisibility(View.GONE);

                if (!newsItems.isEmpty()) {
                    recyclerView.setAdapter(adapter);

                    emptyStateTextView.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    textViewTitle.setVisibility(View.VISIBLE);
                    adapter.submitList(newsItems);
                }

                if (newsItems.isEmpty()) {
                    initEmptyRecyclerView();

                    adapter.submitList(newsItems);
                    textViewTitle.setVisibility(View.INVISIBLE);
                    emptyStateTextView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    emptyStateTextView.setText(R.string.no_news_found);
                }

            }
        });
    }

    public void initEmptyRecyclerView() {

        adapter = new NewsItemAdapterV2(mContext);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (mContext, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        searchKeywordFromSearchView(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchKeywordFromSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("Search Latest News...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    keyword = query;
                    initEmptyRecyclerView();
                    progressBar.setVisibility(View.VISIBLE);
                    textViewTitle.setVisibility(View.INVISIBLE);
                    mNewsViewModel.fetchData(query, getString(R.string.API_KEY_2));
                } else {
                    Toast.makeText(mContext, "Type more than two letters!", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("keyword", keyword);
    }
}