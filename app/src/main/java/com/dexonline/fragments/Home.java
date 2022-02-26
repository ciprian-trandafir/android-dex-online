package com.dexonline.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dexonline.R;
import com.dexonline.adapter.WordOfDayAdapter;
import com.dexonline.classes.Definition;
import com.dexonline.classes.WordOfDay;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Type;
import java.util.List;

public class Home extends Fragment {

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();

        String json = sharedPrefs.getString("listOthersWordOfDay", "");
        Type type = new TypeToken<List<WordOfDay>>(){}.getType();
        List<WordOfDay> listOthersWordOfDay = gson.fromJson(json, type);

        String json_1 = sharedPrefs.getString("WordOfDayDefinition", "");
        Type type_1 = new TypeToken<Definition>(){}.getType();
        Definition wordOfDayDefinition = gson.fromJson(json_1, type_1);

        String json_2 = sharedPrefs.getString("WordOfDay", "");
        Type type_2 = new TypeToken<WordOfDay>(){}.getType();
        WordOfDay wordOfDay = gson.fromJson(json_2, type_2);

        ImageView imageWordOfDay = view.findViewById(R.id.wordOfDayImage);
        Picasso.get().load(wordOfDay.getImage()).into(imageWordOfDay);

        TextView wordOfDayReason, wordOfDayText, wordOfDayDefinitionText, wordOfDayDefinitionSource, wordOfDayDefinitionAddedBy;

        wordOfDayReason = view.findViewById(R.id.wordOfDayReason);
        wordOfDayReason.setText(wordOfDay.getReason());

        wordOfDayText = view.findViewById(R.id.wordOfDay);
        wordOfDayText.setText(wordOfDay.getWord());

        //de facut aici misto in caz ca n are net
        wordOfDayDefinitionText = view.findViewById(R.id.wordOfDayDefinition);
        wordOfDayDefinitionText.setText(Html.fromHtml(wordOfDayDefinition.getHtmlRep()));

        wordOfDayDefinitionSource = view.findViewById(R.id.wordOfDayDefinitionSource);
        wordOfDayDefinitionSource.setText(wordOfDayDefinition.getSourceName());
        wordOfDayDefinitionSource.setOnClickListener(v -> {
            String path = wordOfDayDefinition.getSourceName();
            path = path.replaceAll("'", "");
            path = path.replaceAll(" ", "");
            path = path.toLowerCase().trim();
            path = path.equals("dex98") ? "dex" : path;

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://dexonline.ro/sursa/" + path));
            startActivity(browserIntent);
        });

        wordOfDayDefinitionAddedBy = view.findViewById(R.id.wordOfDayDefinitionAddedBy);
        wordOfDayDefinitionAddedBy.setText(wordOfDayDefinition.getUserNick());
        wordOfDayDefinitionAddedBy.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://dexonline.ro/utilizator/" + wordOfDayDefinition.getUserNick()));
            startActivity(browserIntent);
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWordOfDay);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), 0));
        WordOfDayAdapter wordOfDayAdapter = new WordOfDayAdapter(listOthersWordOfDay);
        recyclerView.setAdapter(wordOfDayAdapter);
    }
}