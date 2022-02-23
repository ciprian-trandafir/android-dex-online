package com.dexonline.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dexonline.R;
import com.dexonline.adapter.SearchHistoryAdapter;
import com.dexonline.classes.Search;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchHistory extends AppCompatActivity {
    List<Search> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        ImageView back = findViewById(R.id.searchHistoryBack);
        back.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, android.R.anim.fade_out);
        });

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("searchHistory", "");
        Type type = new TypeToken<List<Search>>(){}.getType();
        searchList = gson.fromJson(json, type);

        if (searchList == null) {
            searchList = new ArrayList<>();
        }

        Collections.reverse(searchList);

        RecyclerView recyclerView = findViewById(R.id.recyclerSearchHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(searchList);
        recyclerView.setAdapter(searchHistoryAdapter);
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}