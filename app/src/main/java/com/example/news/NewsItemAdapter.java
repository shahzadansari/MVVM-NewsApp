package com.example.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsItem.R;

public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.ViewHolder> {

    private Context mContext;
    private NewsItem mNewsItem;

    public NewsItemAdapter(Context mContext, NewsItem mNewsItem) {
        this.mContext = mContext;
        this.mNewsItem = mNewsItem;
    }

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
        Glide.with(mContext)
                .load(mNewsItem.getUrlToImage())
                .into(holder.imageView);

        holder.textViewTitle.setText(mNewsItem.getTitle());

        holder.textViewDescription.setText(mNewsItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textViewTitle;
        private TextView textViewDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.title_image);
            textViewTitle = itemView.findViewById(R.id.news_title);
            textViewDescription = itemView.findViewById(R.id.news_description);
        }
    }

}
