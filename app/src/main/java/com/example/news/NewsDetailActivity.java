package com.example.news;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.news.utils.Utils;
import com.example.newsItem.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class NewsDetailActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    private Toolbar toolbar;
    private ImageView titleImageView;
    private TextView appbarTitleTextView;
    private TextView appbarSubtitleTextView;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView datePublishedTextView;
    private String mUrl;
    private String mUrlToImage;
    private String mTitle;
    private String mDate;
    private String mSource;
    private String mAuthor;
    private boolean isHideToolbarView = false;
    private LinearLayout titleAppbar;
    private AppBarLayout appBarLayout;
    private FrameLayout frameLayoutDateBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        appBarLayout = findViewById(R.id.layout_appbar);
        appBarLayout.addOnOffsetChangedListener(this);

        frameLayoutDateBehavior = findViewById(R.id.date_behavior);
        titleAppbar = findViewById(R.id.layout_title_appbar);
        titleImageView = findViewById(R.id.image_view_title);
        appbarTitleTextView = findViewById(R.id.title_on_layout_title_appbar);
        appbarSubtitleTextView = findViewById(R.id.subtitle_on_layout_title_appbar);
        authorTextView = findViewById(R.id.text_view_source_author_time);
        titleTextView = findViewById(R.id.text_view_title_news);
        datePublishedTextView = findViewById(R.id.text_view_date_published);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mUrlToImage = intent.getStringExtra("urlToImage");
        mTitle = intent.getStringExtra("title");
        mDate = intent.getStringExtra("date");
        mSource = intent.getStringExtra("source");
        mAuthor = intent.getStringExtra("author");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawableColor());

        Glide.with(this)
                .load(mUrlToImage)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(titleImageView);

        appbarTitleTextView.setText(mSource);
        appbarSubtitleTextView.setText(mUrl);
        titleTextView.setText(mTitle);
        authorTextView.setText(mSource + appendAuthorWithBullet(mAuthor));
        datePublishedTextView.setText(Utils.DateFormat(mDate));

        initWebView(mUrl);

    }

    private String appendAuthorWithBullet(String mAuthor) {

        String author;

        if (this.mAuthor != null) {
            author = " \u2022 " + this.mAuthor;
        } else {
            author = "";
        }

        return author;
    }

    private void initWebView(String url) {

        WebView webView = findViewById(R.id.web_view);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            frameLayoutDateBehavior.setVisibility(View.GONE);
            titleAppbar.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            frameLayoutDateBehavior.setVisibility(View.VISIBLE);
            titleAppbar.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.open_in_browser) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mUrl));
            startActivity(intent);
            return true;

        } else if (id == R.id.share) {

            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plan");
                intent.putExtra(Intent.EXTRA_SUBJECT, mSource);
                String body = mTitle + "\n" + mUrl + "\n" + "Via News App." + "\n";
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intent, "Share with :"));

            } catch (Exception e) {
                Toast.makeText(this, "Oops! \nCannot share this news", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}