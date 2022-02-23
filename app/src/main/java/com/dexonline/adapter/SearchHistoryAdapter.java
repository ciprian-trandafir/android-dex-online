package com.dexonline.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dexonline.R;
import com.dexonline.classes.Search;
import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryAdapterVh>{
    private static List<Search> searchList;

    public SearchHistoryAdapter(List<Search> searchList_) {
        searchList = searchList_;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public SearchHistoryAdapter.SearchHistoryAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return new SearchHistoryAdapterVh(LayoutInflater.from(context).inflate(R.layout.layout_search_history, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SearchHistoryAdapter.SearchHistoryAdapterVh holder, int position) {
        Search search = searchList.get(position);

        holder.searchHistoryWord.setText(search.getText());
        holder.searchHistoryTime.setText(search.getDate());
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public static class SearchHistoryAdapterVh extends RecyclerView.ViewHolder {
        TextView searchHistoryWord, searchHistoryTime;
        public SearchHistoryAdapterVh(@NonNull View itemView) {
            super(itemView);
            searchHistoryWord = itemView.findViewById(R.id.searchHistoryWord);
            searchHistoryTime = itemView.findViewById(R.id.searchHistoryTime);
        }
    }
}
