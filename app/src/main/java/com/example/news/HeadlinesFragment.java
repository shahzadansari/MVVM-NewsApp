package com.example.news;

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
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.adapters.NewsItemAdapter;
import com.example.news.api.NewsAPI;
import com.example.news.models.NewsItem;
import com.example.news.models.RootJsonData;
import com.example.newsItem.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeadlinesFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private NewsItemAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyStateTextView;
    private TextView textViewTitle;
    private Context mContext;
    private String category = "";
    private SwipeRefreshLayout swipeRefreshLayout;
    private String language = "";
    private boolean isLanguageAvailable = false;
    private Spinner spinner;
    private CardView cardView;

    public HeadlinesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        language = Locale.getDefault().getLanguage();
        isLanguageAvailable = Utils.checkLanguage(language);
        if (isLanguageAvailable) {
            language = Utils.getLanguage();
        } else {
            language = "en";
        }
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

        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                R.array.categories_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        if (savedInstanceState != null) {
            category = savedInstanceState.getString("category");
        }

        initEmptyRecyclerView();
        fetchData(category);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchData(category));
        return rootView;
    }

    public void initEmptyRecyclerView() {

        adapter = new NewsItemAdapter(mContext, new ArrayList<NewsItem>(), (MainActivity) getActivity());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (mContext, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void fetchData(String category) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            Call<RootJsonData> rootJsonDataCall = createJsonDataCall(category);
            rootJsonDataCall.enqueue(new Callback<RootJsonData>() {
                @Override
                public void onResponse(Call<RootJsonData> call, Response<RootJsonData> response) {
                    cardView.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    textViewTitle.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    initRecyclerViewWithResponseData(response);
                }

                @Override
                public void onFailure(Call<RootJsonData> call, Throwable t) {
                    cardView.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                    textViewTitle.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    emptyStateTextView.setText(t.getMessage());
                }
            });

        } else {
            progressBar.setVisibility(View.GONE);
            textViewTitle.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    public Call<RootJsonData> createJsonDataCall(String category) {

        NewsAPI newsAPI = ServiceGenerator.createService(NewsAPI.class);
        Call<RootJsonData> rootJsonDataCall;

        rootJsonDataCall = newsAPI.getTopHeadlinesByCategory(category, language, getString(R.string.API_KEY));

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
            adapter = new NewsItemAdapter(mContext, newsItemList, (MainActivity) getActivity());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = parent.getItemAtPosition(position).toString().toLowerCase();

        initEmptyRecyclerView();
        progressBar.setVisibility(View.VISIBLE);
        fetchData(category);
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