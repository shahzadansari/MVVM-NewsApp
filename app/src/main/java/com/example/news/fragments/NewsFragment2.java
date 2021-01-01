package com.example.news.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.MainActivity;
import com.example.news.adapters.NewsItemAdapter;
import com.example.news.models.NewsItem;
import com.example.news.utils.Utils;
import com.example.news.viewmodels.NewsViewModel;
import com.example.newsItem.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NewsFragment2 extends Fragment {

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

    private List<NewsItem> newsItemList;
    private static final String TAG = "NewsFragment2";

    private NewsViewModel mNewsViewModel;

    public NewsFragment2() {
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

        locale = Utils.getCountry();
        isLocaleAvailable = Utils.checkLocale(locale);

        language = Locale.getDefault().getLanguage();
        isLanguageAvailable = Utils.checkLanguage(language);

        mContext = getActivity();
        progressBar = rootView.findViewById(R.id.progress_circular);
        emptyStateTextView = rootView.findViewById(R.id.empty_view);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        textViewTitle = rootView.findViewById(R.id.text_view_top_headlines);
        recyclerView = rootView.findViewById(R.id.recycler_view);

        if (savedInstanceState != null) {
            keyword = savedInstanceState.getString("keyword");
        }

        initEmptyRecyclerView();

        mNewsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        mNewsViewModel.getNewsItemListObserver().observe(getViewLifecycleOwner(), new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(List<NewsItem> newsItems) {
                Log.d(TAG, "onChanged: called");
                if (newsItems != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                    textViewTitle.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    newsItemList = newsItems;
                    adapter = new NewsItemAdapter(mContext, newsItemList, (MainActivity) getActivity());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onChanged: newsItems: null");
                    emptyStateTextView.setText(R.string.no_news_found);
                }

            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mNewsViewModel.fetchData("amazon", getString(R.string.API_KEY));
        } else {
            progressBar.setVisibility(View.GONE);
            textViewTitle.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }


//        fetchData(keyword);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            mNewsViewModel.fetchData("amazon", getString(R.string.API_KEY));
        });

        setHasOptionsMenu(true);
        return rootView;
    }

    public void initEmptyRecyclerView() {

        adapter = new NewsItemAdapter(mContext, new ArrayList<NewsItem>(), (MainActivity) getActivity());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (mContext, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("keyword", keyword);
    }
}