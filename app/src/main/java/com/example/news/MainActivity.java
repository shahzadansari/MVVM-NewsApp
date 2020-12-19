package com.example.news;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newsItem.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, new NewsFragment())
                    .setReorderingAllowed(true)
                    .commit();
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.page_news:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new NewsFragment())
                            .setReorderingAllowed(true)
                            .commit();
                    break;
                case R.id.page_headlines:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new HeadlinesFragment())
                            .setReorderingAllowed(true)
                            .commit();
                    break;
                case R.id.page_articles:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new ArticlesFragment())
                            .setReorderingAllowed(true)
                            .commit();
                    break;
                case R.id.page_favorites:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new FavoritesFragment())
                            .setReorderingAllowed(true)
                            .commit();
                    break;
                default:
                    break;
            }
            return true;
        });
    }
}