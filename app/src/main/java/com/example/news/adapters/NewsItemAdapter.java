package com.example.news.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.news.models.NewsItem;
import com.example.newsItem.R;

import java.util.List;

public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.ViewHolder> {

    private Context mContext;
    private List<NewsItem> newsItemList;

    public NewsItemAdapter(Context mContext, List<NewsItem> newsItemList) {
        this.mContext = mContext;
        this.newsItemList = newsItemList;
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
                .load(newsItemList.get(position).getUrlToImage())
                .into(holder.imageView);

        holder.textViewTitle.setText(newsItemList.get(position).getTitle());

        holder.textViewDescription.setText(newsItemList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return newsItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private ImageView imageView;
        private TextView textViewTitle;
        private TextView textViewDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.title_image);
            textViewTitle = itemView.findViewById(R.id.news_title);
            textViewDescription = itemView.findViewById(R.id.news_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            Uri newsItemUri = Uri.parse(newsItemList.get(position).getUrl());
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsItemUri);
            mContext.startActivity(websiteIntent);
        }
    }

}
