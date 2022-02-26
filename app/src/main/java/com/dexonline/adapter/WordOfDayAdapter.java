package com.dexonline.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dexonline.R;
import com.dexonline.classes.WordOfDay;
import com.squareup.picasso.Picasso;
import java.util.List;

public class WordOfDayAdapter extends RecyclerView.Adapter<WordOfDayAdapter.WordOfDayAdapterVh>{
    private static List<WordOfDay> wordOfDayList;

    public WordOfDayAdapter(List<WordOfDay> wordOfDayList_) {
        wordOfDayList = wordOfDayList_;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public WordOfDayAdapter.WordOfDayAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return new WordOfDayAdapterVh(LayoutInflater.from(context).inflate(R.layout.layout_word_of_day, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WordOfDayAdapter.WordOfDayAdapterVh holder, int position) {
        WordOfDay wordOfDay = wordOfDayList.get(position);

        Picasso.get().load(wordOfDay.getImage()).into(holder.wordOfDayImageRW);
        holder.wordOfDayTitleRW.setText(holder.wordOfDayTitleRW.getText() + " - " + wordOfDay.getYear());
        holder.wordOfDayRW.setText(wordOfDay.getWord());
        holder.wordOfDayReasonRW.setText(wordOfDay.getReason());
    }

    @Override
    public int getItemCount() {
        return wordOfDayList.size();
    }

    public static class WordOfDayAdapterVh extends RecyclerView.ViewHolder {
        ImageView wordOfDayImageRW;
        TextView wordOfDayTitleRW, wordOfDayRW, wordOfDayReasonRW;
        public WordOfDayAdapterVh(@NonNull View itemView) {
            super(itemView);
            wordOfDayImageRW = itemView.findViewById(R.id.wordOfDayImageRW);
            wordOfDayTitleRW = itemView.findViewById(R.id.wordOfDayTitleRW);
            wordOfDayRW = itemView.findViewById(R.id.wordOfDayRW);
            wordOfDayReasonRW = itemView.findViewById(R.id.wordOfDayReasonRW);
        }
    }
}
