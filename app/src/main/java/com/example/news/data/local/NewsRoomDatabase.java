package com.example.news.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.news.models.NewsItem;

@Database(entities = {NewsItem.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class NewsRoomDatabase extends RoomDatabase {

    public abstract ArticleDao articleDao();

    public static NewsRoomDatabase INSTANCE;

    public static NewsRoomDatabase getDatabase(Context context) {

        if (INSTANCE == null) {
            synchronized (NewsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NewsRoomDatabase.class, "news_db")
//                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

//    private static Callback sRoomDatabaseCallback = new Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            new PopulateDbAsyncTask(INSTANCE).execute();
//        }
//    };
}