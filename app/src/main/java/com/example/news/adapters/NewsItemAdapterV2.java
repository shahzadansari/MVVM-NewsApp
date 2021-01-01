package com.example.news.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.news.MainActivity;
import com.example.news.NewsDetailActivity;
import com.example.news.models.NewsItem;
import com.example.news.utils.Utils;
import com.example.newsItem.R;

import java.util.List;

public class NewsItemAdapterV2 extends ListAdapter<NewsItem, NewsItemAdapterV2.ViewHolder> {

    private Context mContext;
    private List<NewsItem> newsItemList;
    private static final String TAG = "NewsItemAdapter";

    private static final DiffUtil.ItemCallback<NewsItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<NewsItem>() {
        @Override
        public boolean areItemsTheSame(NewsItem oldItem, NewsItem newItem) {
            return oldItem.getUrl().equals(newItem.getUrl());
        }

        @Override
        public boolean areContentsTheSame(NewsItem oldItem, NewsItem newItem) {
            return (oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getAuthor().equals(newItem.getAuthor()))
                    && oldItem.getContent().equals(newItem.getContent())
                    && oldItem.getPublishedAt().equals(newItem.getPublishedAt())
                    && oldItem.getUrlToImage().equals(newItem.getUrlToImage())
                    && oldItem.getUrl().equals(newItem.getUrl())
                    && oldItem.getSource().getId().equals(newItem.getSource().getId())
                    && oldItem.getSource().getName().equals(newItem.getSource().getName());
        }
    };

    // For transition animation
    private MainActivity main_activity;

    public NewsItemAdapterV2(Context context) {
        super(DIFF_CALLBACK);
        mContext = context;
    }

//    public NewsItemAdapterV2(Context mContext, List<NewsItem> newsItemList, MainActivity mainActivity) {
//        this.mContext = mContext;
//        this.newsItemList = newsItemList;
//        this.main_activity = mainActivity;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NewsItem newsItem = getItem(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawableColor());
        requestOptions.error(Utils.getRandomDrawableColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(mContext)
                .load(newsItem.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBarInImage.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBarInImage.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.titleImage);

        holder.textViewTitle.setText(newsItem.getTitle());
        holder.textViewDescription.setText(newsItem.getDescription());
        holder.textViewSource.setText(newsItem.getSource().getName());
        holder.textViewTime.setText(" \u2022 " + Utils.DateToTimeFormat(newsItem.getPublishedAt()));
        Log.d(TAG, "onChanged: called");
        holder.textViewPublishedAt.setText(Utils.DateFormat(newsItem.getPublishedAt()));
        holder.textViewAuthor.setText(newsItem.getAuthor());
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private ImageView titleImage;
        private TextView textViewAuthor;
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewSource;
        private TextView textViewTime;
        private TextView textViewPublishedAt;
        private ProgressBar progressBarInImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleImage = itemView.findViewById(R.id.image_view_title);
            textViewTitle = itemView.findViewById(R.id.text_view_news_title);
            textViewAuthor = itemView.findViewById(R.id.text_view_source_author_time);
            textViewSource = itemView.findViewById(R.id.text_view_source);
            textViewTime = itemView.findViewById(R.id.text_view_time);
            textViewDescription = itemView.findViewById(R.id.text_view_news_description);
            textViewPublishedAt = itemView.findViewById(R.id.text_view_publishedAt);
            progressBarInImage = itemView.findViewById(R.id.progress_bar_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(mContext, NewsDetailActivity.class);

            int position = getAdapterPosition();
            intent.putExtra("url", newsItemList.get(position).getUrl());
            intent.putExtra("title", newsItemList.get(position).getTitle());
            intent.putExtra("urlToImage", newsItemList.get(position).getUrlToImage());
            intent.putExtra("date", newsItemList.get(position).getPublishedAt());
            intent.putExtra("source", newsItemList.get(position).getSource().getName());
            intent.putExtra("author", newsItemList.get(position).getAuthor());

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation
                    (main_activity, titleImage, ViewCompat.getTransitionName(titleImage));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mContext.startActivity(intent, options.toBundle());
            } else {
                mContext.startActivity(intent);
            }
        }
    }

}
