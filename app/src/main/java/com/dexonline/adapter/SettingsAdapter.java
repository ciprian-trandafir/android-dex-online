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
import com.dexonline.classes.Setting;
import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsAdapterVh>{
    private static List<Setting> settingList;
    private static SelectedSetting selectedSetting = null;
    private final Context context;

    public SettingsAdapter(List<Setting> settingList_, SelectedSetting selectedSetting_, Context context_) {
        settingList = settingList_;
        selectedSetting = selectedSetting_;
        context = context_;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public SettingsAdapter.SettingsAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return new SettingsAdapterVh(LayoutInflater.from(context).inflate(R.layout.layout_settings, parent, false));
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull SettingsAdapter.SettingsAdapterVh holder, int position) {
        Setting setting = settingList.get(position);
        String name = setting.getName();
        String icon = setting.getIcon();

        String uri = "@drawable/" + icon;
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());

        holder.iconLeft.setImageDrawable(context.getDrawable(imageResource));
        holder.settingName.setText(name);
    }

    @Override
    public int getItemCount() {
        return settingList.size();
    }

    public interface SelectedSetting {
        void selectedSetting(Setting setting);
    }

    public static class SettingsAdapterVh extends RecyclerView.ViewHolder {
        ImageView iconLeft;
        TextView settingName;
        public SettingsAdapterVh(@NonNull View itemView) {
            super(itemView);
            iconLeft = itemView.findViewById(R.id.iconSetting);
            settingName = itemView.findViewById(R.id.settingName);

            itemView.setOnClickListener(view -> selectedSetting.selectedSetting(settingList.get(getAbsoluteAdapterPosition())));
        }
    }
}
