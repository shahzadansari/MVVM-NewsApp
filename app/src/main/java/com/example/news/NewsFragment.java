package com.example.news;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class NewsFragment extends Fragment {

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

    public NewsFragment() {
        // Required empty public constructor
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
        fetchData(keyword);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchData(keyword));
        return rootView;
    }

    public void initEmptyRecyclerView() {

        adapter = new NewsItemAdapter(mContext, new ArrayList<NewsItem>(), (MainActivity) getActivity());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (mContext, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void fetchData(String keyword) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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
            adapter = new NewsItemAdapter(mContext, newsItemList, (MainActivity) getActivity());
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
}