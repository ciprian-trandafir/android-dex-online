package com.dexonline.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import org.jsoup.Jsoup;
import java.util.List;

public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.DefinitionAdapterVh>{
    private static List<Definition> definitionList;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public DefinitionAdapter(List<Definition> definitionList_, Context context_) {
        definitionList = definitionList_;
        context = context_;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public DefinitionAdapter.DefinitionAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return new DefinitionAdapterVh(LayoutInflater.from(context).inflate(R.layout.layout_definition, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DefinitionAdapter.DefinitionAdapterVh holder, int position) {
        Definition definition = definitionList.get(position);

        holder.wordDefinition.setText(Html.fromHtml(definition.getHtmlRep()));
        holder.definitionSource.setText(definition.getSourceName());
        holder.definitionAddedBy.setText(definition.getUserNick());
    }

    @Override
    public int getItemCount() {
        return definitionList.size();
    }

    public static class DefinitionAdapterVh extends RecyclerView.ViewHolder {
        TextView wordDefinition, definitionSource, definitionAddedBy;
        public DefinitionAdapterVh(@NonNull View itemView) {
            super(itemView);
            wordDefinition = itemView.findViewById(R.id.wordDefinition);
            definitionSource = itemView.findViewById(R.id.definitionSource);
            definitionAddedBy = itemView.findViewById(R.id.definitionAddedBy);

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
        }
    }
}
