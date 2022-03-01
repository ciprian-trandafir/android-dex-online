package com.dexonline.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dexonline.R;
import com.dexonline.activities.AboutUs;
import com.dexonline.activities.SearchHistory;
import com.dexonline.adapter.SettingsAdapter;
import com.dexonline.classes.Setting;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Settings extends Fragment implements SettingsAdapter.SelectedSetting {
    private final List<Setting> settingList = new ArrayList<>();
    private String theme;
    private int themeIndex;

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            settingList.add(new Setting("theme", "Aspect general", "ic_dark_mode"));
            SharedPreferences preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireActivity());
            switch (preferences.getString("theme", "default")) {
                case "default":
                    themeIndex = 0;
                    break;
                case "light":
                    themeIndex = 1;
                    break;
                case "dark":
                    themeIndex = 2;
                    break;
            }
        }
        settingList.add(new Setting("history", "Istoric căutări", "ic_history"));
        settingList.add(new Setting("contact", "Despre aplicație", "ic_info"));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSettings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        SettingsAdapter settingsAdapter = new SettingsAdapter(settingList, this, getContext());
        recyclerView.setAdapter(settingsAdapter);
    }

    @Override
    public void selectedSetting(Setting setting) {
        switch (setting.getId()) {
            case "theme":
                themeUpdate();
                break;
            case "history":
                startActivity(new Intent(getActivity(), SearchHistory.class));
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case "contact":
                startActivity(new Intent(getActivity(), AboutUs.class));
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    private void themeUpdate() {
        String[] items = {"Tema dispozitivului", "Luminos", "Întunecat"};
        Dialog dialog;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Aspect general")
            .setSingleChoiceItems(items, themeIndex, (dialogg, which) -> {
                switch (which) {
                    case 0:
                        theme = "default";
                        break;
                    case 1:
                        theme = "light";
                        break;
                    case 2:
                        theme = "dark";
                        break;
                }
            })
            .setPositiveButton("OK", (dialogg, which) -> {
                switch (theme) {//bug aici
                    case "default":
                        setTheme("default");
                        themeIndex = 0;
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        break;
                    case "light":
                        setTheme("light");
                        themeIndex = 1;
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        break;
                    case "dark":
                        setTheme("dark");
                        themeIndex = 2;
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        break;
                }
            })
            .setNegativeButton("Cancel", (dialogg, which) -> dialogg.dismiss());
        dialog = alertDialog.create();
        dialog.show();
    }

    private void setTheme(String name) {
        SharedPreferences preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireActivity());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("theme", name);
        editor.putBoolean("anotherActivity", false);
        editor.apply();
    }
}
