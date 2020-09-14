package com.example.news;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.newsItem.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class NewsDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private Toolbar toolbar;
    private ImageView imageView;
    private TextView appbar_title;
    private TextView appbar_subtitle;
    private TextView titleTextView;
    private TextView authorTextView;
    private String mUrl;
    private String mImg;
    private String mTitle;
    private String mDate;
    private String mSource;
    private String mAuthor;
    private boolean isHideToolbarView = false;
    private LinearLayout titleAppbar;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);

        titleAppbar = findViewById(R.id.title_appbar);
        imageView = findViewById(R.id.image);
        appbar_title = findViewById(R.id.title_on_appbar);
        appbar_subtitle = findViewById(R.id.subtitle_on_appbar);
        authorTextView = findViewById(R.id.author);
        titleTextView = findViewById(R.id.title);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mImg = intent.getStringExtra("urlToImage");
        mTitle = intent.getStringExtra("title");
        mDate = intent.getStringExtra("date");
        mSource = intent.getStringExtra("source");
        mAuthor = intent.getStringExtra("author");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawableColor());

        Glide.with(this)
                .load(mImg)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

        appbar_title.setText(mSource);
        appbar_subtitle.setText(mUrl);

        String author;
        if (mAuthor != null) {
            author = " \u2022 " + mAuthor;
        } else {
            author = "";
        }

        titleTextView.setText(mTitle);
        authorTextView.setText(mSource + author);

        initWebView(mUrl);

    }

    private void initWebView(String url) {

        WebView webView = findViewById(R.id.webView);
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
            titleAppbar.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
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

        if (id == R.id.view_in_browser) {
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