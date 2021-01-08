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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.viewmodels.ArticlesViewModel;
import com.example.news.adapters.NewsItemAdapter;
import com.example.news.models.NewsItem;
import com.example.news.utils.DataStatus;
import com.example.newsItem.R;

public class ArticlesFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewsItemAdapter adapter;

    private TextView emptyStateTextView;
    private TextView textViewTitle;

    private Context mContext;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String keyword = "";

    private ArticlesViewModel articlesViewModel;

    public ArticlesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Articles");
        keyword = "news";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_articles, container, false);

        mContext = getActivity();
        emptyStateTextView = rootView.findViewById(R.id.empty_view);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        textViewTitle = rootView.findViewById(R.id.text_view_top_headlines);
        recyclerView = rootView.findViewById(R.id.recycler_view);

        if (savedInstanceState != null) {
            keyword = savedInstanceState.getString("keyword");
        }

        initEmptyRecyclerView();

        articlesViewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);
        subscribeObservers();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            articlesViewModel.setKeyword(keyword);
        });

        setHasOptionsMenu(true);
        return rootView;
    }

    private void subscribeObservers() {

        articlesViewModel.itemPagedList.observe(getViewLifecycleOwner(), new Observer<PagedList<NewsItem>>() {
            @Override
            public void onChanged(PagedList<NewsItem> newsItems) {
                adapter.submitList(newsItems);
            }
        });
        articlesViewModel.getDataStatus().observe(getViewLifecycleOwner(), new Observer<DataStatus>() {
            @Override
            public void onChanged(DataStatus dataStatus) {
                switch (dataStatus) {
                    case LOADED:
                        emptyStateTextView.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                        textViewTitle.setVisibility(View.VISIBLE);
                        break;
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        textViewTitle.setVisibility(View.INVISIBLE);
                        emptyStateTextView.setVisibility(View.INVISIBLE);
                        break;
                    case EMPTY:
                        swipeRefreshLayout.setRefreshing(false);
                        textViewTitle.setVisibility(View.INVISIBLE);
                        emptyStateTextView.setVisibility(View.VISIBLE);
                        emptyStateTextView.setText(R.string.no_news_found);
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        textViewTitle.setVisibility(View.INVISIBLE);
                        emptyStateTextView.setVisibility(View.VISIBLE);
                        emptyStateTextView.setText(R.string.no_internet_connection);
                        break;
                }
            }
        });
    }

    private void handleUIChanges(PagedList<NewsItem> newsItems) {
        if (!newsItems.isEmpty()) {
            emptyStateTextView.setVisibility(View.INVISIBLE);
            swipeRefreshLayout.setRefreshing(false);
            textViewTitle.setVisibility(View.VISIBLE);
        }

        if (newsItems.isEmpty()) {
            textViewTitle.setVisibility(View.INVISIBLE);
            emptyStateTextView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
            emptyStateTextView.setText(R.string.no_news_found);
        }
    }

    public boolean checkNetworkConnection() {
        boolean isNetworkAvailable = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            isNetworkAvailable = true;
        }
        return isNetworkAvailable;
    }

    public void initEmptyRecyclerView() {

        adapter = new NewsItemAdapter(mContext);
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
                    swipeRefreshLayout.setRefreshing(true);
                    textViewTitle.setVisibility(View.INVISIBLE);
                    articlesViewModel.setKeyword(query);
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
                keyword = "news";
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