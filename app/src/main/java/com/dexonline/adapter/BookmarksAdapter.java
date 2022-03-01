package com.dexonline.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dexonline.R;
import com.dexonline.classes.Definition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;
import java.lang.reflect.Type;
import java.util.List;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.BookmarksAdapterVh>{
    private static List<Definition> definitionList;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public BookmarksAdapter(List<Definition> definitionList_, Context context_) {
        definitionList = definitionList_;
        context = context_;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public BookmarksAdapter.BookmarksAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return new BookmarksAdapterVh(LayoutInflater.from(context).inflate(R.layout.layout_definition, parent, false));
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull BookmarksAdapter.BookmarksAdapterVh holder, int position) {
        Definition definition = definitionList.get(position);

        if (definition.isBookmarked()) {
            holder.saveBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmarked));
        } else {
            holder.saveBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark));
        }

        holder.wordDefinition.setText(Html.fromHtml(definition.getHtmlRep()));
        holder.definitionSource.setText(definition.getSourceName());
        holder.definitionAddedBy.setText(definition.getUserNick());
    }

    @Override
    public int getItemCount() {
        return definitionList.size();
    }

    public class BookmarksAdapterVh extends RecyclerView.ViewHolder {
        TextView wordDefinition, definitionSource, definitionAddedBy;
        ImageView saveBookmark;
        @SuppressLint("UseCompatLoadingForDrawables")
        public BookmarksAdapterVh(@NonNull View itemView) {
            super(itemView);
            wordDefinition = itemView.findViewById(R.id.wordDefinition);
            definitionSource = itemView.findViewById(R.id.definitionSource);
            definitionAddedBy = itemView.findViewById(R.id.definitionAddedBy);
            saveBookmark = itemView.findViewById(R.id.definitionSaveBookmark);

            definitionSource.setOnClickListener(v -> {
                String path = definitionSource.getText().toString();
                path = path.replaceAll("'", "");
                path = path.replaceAll(" ", "");
                path = path.toLowerCase().trim();
                path = path.equals("dex98") ? "dex" : path;

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://dexonline.ro/sursa/" + path));
                context.startActivity(browserIntent);
            });

            definitionAddedBy.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://dexonline.ro/utilizator/" + definitionAddedBy.getText()));
                context.startActivity(browserIntent);
            });

            ImageView copyClipboard = itemView.findViewById(R.id.definitionSaveClipboard);
            copyClipboard.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("definition", Jsoup.parse(definitionList.get(getAbsoluteAdapterPosition()).getHtmlRep()).text());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "Copiat in clipboard!", Toast.LENGTH_SHORT).show();
            });

            saveBookmark.setOnClickListener(v -> {
                saveBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark));

                int position = getAbsoluteAdapterPosition();
                Definition definition = definitionList.get(position);
                SharedPreferences sharedPrefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = sharedPrefs.getString("bookmarkedIds", "");
                Type type = new TypeToken<List<String>>(){}.getType();
                List<String> bookmarkedIds = gson.fromJson(json, type);

                String json_ = sharedPrefs.getString("bookmarkedDefinitions", "");
                Type type_ = new TypeToken<List<Definition>>(){}.getType();
                List<Definition> bookmarkedDefinitions = gson.fromJson(json_, type_);

                bookmarkedDefinitions.remove(bookmarkedIds.indexOf(definition.getId()));
                bookmarkedIds.remove(definition.getId());

                String bookmarkedIds_ = gson.toJson(bookmarkedIds);
                editor.putString("bookmarkedIds", bookmarkedIds_);

                String bookmarkedDefinitions_ = gson.toJson(bookmarkedDefinitions);
                editor.putString("bookmarkedDefinitions", bookmarkedDefinitions_);

                editor.apply();

                definitionList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, definitionList.size());
            });
        }
    }
}
