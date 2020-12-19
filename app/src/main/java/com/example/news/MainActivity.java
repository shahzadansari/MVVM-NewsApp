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

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new NewsFragment())
                .setReorderingAllowed(true)
                .commit();

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



//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString("keyword", keyword);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_main, menu);
//
//        searchKeywordFromSearchView(menu);
//
//        return true;
//    }

//    private void searchKeywordFromSearchView(Menu menu) {
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
//
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setQueryHint("Search Latest News...");
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if (query.length() > 2) {
//                    searchKeyword(query);
//                } else {
//                    Toast.makeText(MainActivity.this, "Type more than two letters!", Toast.LENGTH_SHORT).show();
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//
//        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                keyword = "";
//                return true;
//            }
//        });
//
//        searchMenuItem.getIcon().setVisible(false, false);
//    }
}