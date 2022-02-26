package com.dexonline.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dexonline.R;
import com.dexonline.adapter.BookmarksAdapter;
import com.dexonline.classes.Definition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bookmarks extends Fragment {

    public Bookmarks() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }

    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("bookmarkedDefinitions", "");
        Type type = new TypeToken<List<Definition>>(){}.getType();
        List<Definition> bookmarkedDefinitions = gson.fromJson(json, type);
        if (bookmarkedDefinitions == null) {
            bookmarkedDefinitions = new ArrayList<>();
        }

        Collections.reverse(bookmarkedDefinitions);

        if (bookmarkedDefinitions.size() != 0) {
            TextView hint = view.findViewById(R.id.bookmarksHint);
            hint.setVisibility(View.INVISIBLE);
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewBookmarks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        BookmarksAdapter bookmarksAdapter = new BookmarksAdapter(bookmarkedDefinitions, getContext());
        recyclerView.setAdapter(bookmarksAdapter);
    }
}