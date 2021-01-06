package com.example.news.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.adapters.NewsItemAdapter;
import com.example.news.models.NewsItem;
import com.example.news.viewmodels.HeadlinesViewModel;
import com.example.newsItem.R;

import java.util.List;

public class HeadlinesFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private NewsItemAdapter adapter;

    private ProgressBar progressBar;
    private TextView emptyStateTextView;
    private TextView textViewTitle;

    private Context mContext;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String category = "";

    private Spinner spinner;
    private CardView cardView;

    private HeadlinesViewModel mHeadlinesViewModel;

    private int pageNumber;

    public HeadlinesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Headlines");
        category = "business";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_headlines, container, false);

        mContext = getActivity();
        progressBar = rootView.findViewById(R.id.progress_circular);
        emptyStateTextView = rootView.findViewById(R.id.empty_view);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        textViewTitle = rootView.findViewById(R.id.text_view_top_headlines);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        spinner = rootView.findViewById(R.id.spinner_category);
        cardView = rootView.findViewById(R.id.card_view);

        adapter = new NewsItemAdapter(mContext);

        initSpinner();
        initEmptyRecyclerView();

        if (savedInstanceState != null) {
            category = savedInstanceState.getString("category");
        }

        mHeadlinesViewModel = ViewModelProviders.of(this).get(HeadlinesViewModel.class);
        subscribeObservers();

        fetchData();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            initEmptyRecyclerView();
            mHeadlinesViewModel.getHeadlines(category, pageNumber);
        });

        return rootView;
    }

    private void fetchData() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mHeadlinesViewModel.getHeadlines(category, pageNumber);
        } else {
            progressBar.setVisibility(View.GONE);
            textViewTitle.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    private void subscribeObservers() {
        mHeadlinesViewModel.getHeadlinesObserver().observe(getViewLifecycleOwner(), new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(List<NewsItem> newsItems) {
                progressBar.setVisibility(View.GONE);

                if (!newsItems.isEmpty()) {
                    initEmptyRecyclerView();
//                    adapter.submitList(newsItems);

                    cardView.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    emptyStateTextView.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    textViewTitle.setVisibility(View.VISIBLE);
                }

                if (newsItems.isEmpty()) {
                    initEmptyRecyclerView();
//                    adapter.submitList(newsItems);

                    cardView.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                    textViewTitle.setVisibility(View.INVISIBLE);
                    emptyStateTextView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    emptyStateTextView.setText(R.string.no_news_found);
                }
            }
        });
    }

    private void initSpinner() {
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                R.array.categories_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    public void initEmptyRecyclerView() {

        adapter = new NewsItemAdapter(mContext);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (mContext, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = parent.getItemAtPosition(position).toString().toLowerCase();
        initEmptyRecyclerView();
        progressBar.setVisibility(View.VISIBLE);
        mHeadlinesViewModel.getHeadlines(category, pageNumber);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("category", category);
    }
}