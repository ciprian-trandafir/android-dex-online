package com.dexonline.activities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.dexonline.R;
import com.dexonline.fragments.Bookmarks;
import com.dexonline.fragments.Home;
import com.dexonline.fragments.Search;
import com.dexonline.fragments.Settings;
import com.google.android.material.navigation.NavigationBarView;

public class HomePage extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Home fragment = new Home();
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.homePageContent, fragment, "");
        fragmentTransaction1.commit();

        NavigationBarView navigationView = findViewById(R.id.navigation);
        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_settings:
                    Settings settingsFragment = new Settings();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.homePageContent, settingsFragment, "");
                    fragmentTransaction2.commit();
                    return true;
                case R.id.nav_search:
                    Search searchFragment = new Search();
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.homePageContent, searchFragment, "");
                    fragmentTransaction3.commit();
                    return true;
                case R.id.nav_home:
                    Home homeFragment = new Home();
                    FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.homePageContent, homeFragment, "");
                    fragmentTransaction4.commit();
                    return true;
                case R.id.nav_saved:
                    Bookmarks bookmarksFragment = new Bookmarks();
                    FragmentTransaction fragmentTransaction5 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction5.replace(R.id.homePageContent, bookmarksFragment, "");
                    fragmentTransaction5.commit();
                    return true;
            }

            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        boolean anotherActivity = sharedPrefs.getBoolean("anotherActivity", false);

        if (!anotherActivity) {
            NavigationBarView navigationView = findViewById(R.id.navigation);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
    }
}
