package com.example.news.data.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.news.models.NewsItem;

import java.util.List;

@Dao
public interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(NewsItem article);

    @Query("SELECT * FROM saved_articles ORDER BY id DESC")
    LiveData<List<NewsItem>> getAllSavedArticles();

    @Delete
    void deleteArticle(NewsItem article);

    @Query("DELETE FROM saved_articles")
    void deleteAllSavedArticles();
}
