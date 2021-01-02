package com.example.news.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.adapters.NewsItemAdapter;
import com.example.news.models.NewsItem;
import com.example.news.viewmodels.FavoritesViewModel;
import com.example.newsItem.R;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private TextView emptyStateTextView;
    private TextView textViewTitle;

    private RecyclerView recyclerView;
    private NewsItemAdapter adapter;
    private Context mContext;

    private FavoritesViewModel mFavoritesViewModel;
    
    //TODO: Add delete articles on swipe

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Favorites");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        mContext = getActivity();

        emptyStateTextView = rootView.findViewById(R.id.empty_view);
        textViewTitle = rootView.findViewById(R.id.text_view_top_headlines);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new NewsItemAdapter(mContext);
        initEmptyRecyclerView();

        mFavoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        subscribeObservers();

        return rootView;
    }

    private void subscribeObservers() {
        mFavoritesViewModel.getAllSavedArticles().observe(getViewLifecycleOwner(), new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(List<NewsItem> savedArticles) {

                if (!savedArticles.isEmpty()) {
                    adapter.submitList(savedArticles);
                    emptyStateTextView.setVisibility(View.INVISIBLE);
                    textViewTitle.setVisibility(View.VISIBLE);
                }

                if (savedArticles.isEmpty()) {
                    adapter.submitList(savedArticles);
                    emptyStateTextView.setVisibility(View.VISIBLE);
                    textViewTitle.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initEmptyRecyclerView() {
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (mContext, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
    }
}