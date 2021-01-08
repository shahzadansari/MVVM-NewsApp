package com.example.news.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.adapters.NewsItemAdapter;
import com.example.news.models.NewsItem;
import com.example.news.utils.DataStatus;
import com.example.news.viewmodels.HeadlinesViewModel;
import com.example.newsItem.R;

public class HeadlinesFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private NewsItemAdapter adapter;

    private TextView emptyStateTextView;
    private TextView textViewTitle;

    private Context mContext;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String category = "";

    private Spinner spinner;
    private CardView cardView;

    private HeadlinesViewModel mHeadlinesViewModel;

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

        swipeRefreshLayout.setOnRefreshListener(() -> {
            mHeadlinesViewModel.setCategory(category);
        });

        return rootView;
    }

    private void subscribeObservers() {
        mHeadlinesViewModel.itemPagedList.observe(getViewLifecycleOwner(), new Observer<PagedList<NewsItem>>() {
            @Override
            public void onChanged(PagedList<NewsItem> newsItems) {
                adapter.submitList(newsItems);
            }
        });
        mHeadlinesViewModel.getDataStatus().observe(getViewLifecycleOwner(), new Observer<DataStatus>() {
            @Override
            public void onChanged(DataStatus dataStatus) {
                switch (dataStatus) {
                    case LOADED:
                        emptyStateTextView.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                        textViewTitle.setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);
                        break;
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        emptyStateTextView.setVisibility(View.INVISIBLE);
                        textViewTitle.setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);
                        break;
                    case EMPTY:
                        swipeRefreshLayout.setRefreshing(false);
                        textViewTitle.setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);
                        emptyStateTextView.setVisibility(View.VISIBLE);
                        emptyStateTextView.setText(R.string.no_news_found);
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        textViewTitle.setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);
                        emptyStateTextView.setVisibility(View.VISIBLE);
                        emptyStateTextView.setText(R.string.no_internet_connection);
                        break;
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
        mHeadlinesViewModel.setCategory(category);
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