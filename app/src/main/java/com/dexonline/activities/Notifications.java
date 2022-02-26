package com.dexonline.activities;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.dexonline.R;
import com.dexonline.classes.Notification;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Calendar;

public class Notifications extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private Gson gson;
    private Notification notification;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ImageView back = findViewById(R.id.notificationsBack);
        back.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, android.R.anim.fade_out);
        });

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPrefs.edit();
        gson = new Gson();

        String json = sharedPrefs.getString("notification", "");
        Type type = new TypeToken<Notification>(){}.getType();
        notification = gson.fromJson(json, type);

        SwitchCompat switchGeneral = findViewById(R.id.notificationsGeneralSwitch);
        SwitchCompat switchDefinition = findViewById(R.id.notificationsDefinitionSwitch);
        SwitchCompat switchReason = findViewById(R.id.notificationsReasonSwitch);
        TextView notificationTime = findViewById(R.id.notificationsTime);
        LinearLayout moreSettingsWrapper = findViewById(R.id.notificationsMoreSettings);
        moreSettingsWrapper.setOnClickListener(v -> {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, this.getPackageName());
            } else {
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", this.getPackageName());
                intent.putExtra("app_uid", this.getApplicationInfo().uid);
            }
            this.startActivity(intent);
        });

        if (notification.isActive()) {
            switchGeneral.setChecked(true);
        }

        if (notification.isDefinition()) {
            switchDefinition.setChecked(true);
        }

        if (notification.isReason()) {
            switchReason.setChecked(true);
        }

        notificationTime.setText(notification.getTime());

        ConstraintLayout timePickerWrapper = findViewById(R.id.notificationTimeWrapper);
        timePickerWrapper.setOnClickListener(v -> {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker = new TimePickerDialog(this, (timePicker, selectedHour, selectedMinute) -> {
                String time = selectedHour + ":" + selectedMinute;
                notificationTime.setText(time);
                notification.setTime(time);
                updateNotification();
            }, hour, minute, true);
            mTimePicker.show();
        });
        switchGeneral.setOnCheckedChangeListener((buttonView, isChecked) -> {
            notification.setActive(isChecked);
            updateNotification();
        });

        switchDefinition.setOnCheckedChangeListener((buttonView, isChecked) -> {
            notification.setDefinition(isChecked);
            updateNotification();
        });

        switchReason.setOnCheckedChangeListener((buttonView, isChecked) -> {
            notification.setReason(isChecked);
            updateNotification();
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    private void updateNotification() {
        String notification_ = gson.toJson(notification);
        editor.putString("notification", notification_);
        editor.apply();
    }
}
