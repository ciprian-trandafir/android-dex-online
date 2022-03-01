package com.dexonline.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
    private List<Search> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        ImageView back = findViewById(R.id.searchHistoryBack);
        back.setOnClickListener(v -> killActivity());

        SharedPreferences sharedPrefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        editor.putBoolean("anotherActivity", true);
        editor.apply();

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

        TextView hint = findViewById(R.id.searchHistoryHint);

        if (searchList.size() > 0) {
            hint.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        ImageView delete = findViewById(R.id.searchHistoryDelete);
        delete.setOnClickListener(v -> {
            if (searchList.size() > 0) {
                Dialog dialog;
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Sigur doriți să ștergeți istoricul căutărilor?")
                    .setPositiveButton("DA", (dialogg, which) -> {
                        searchList.clear();
                        String searchList_ = gson.toJson(searchList);
                        editor.putString("searchHistory", searchList_);
                        editor.apply();
                        hint.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    })
                    .setNegativeButton("NU", (dialogg, which) -> dialogg.dismiss())
                    .setCancelable(true);
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        killActivity();
    }

    private void killActivity() {
        finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}
