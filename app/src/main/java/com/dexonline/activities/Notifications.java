package com.dexonline.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
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
import com.dexonline.classes.AlarmReceiver;
import com.dexonline.classes.Definition;
import com.dexonline.classes.Helper;
import com.dexonline.classes.Notification;
import com.dexonline.classes.WordOfDay;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;
import java.lang.reflect.Type;
import java.util.Calendar;

public class Notifications extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private Gson gson;
    private Notification notification;

    @SuppressLint({"SetTextI18n", "UnspecifiedImmutableFlag"})
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

                if (notification.isActive()) {
                    startAlarm(this, selectedHour, selectedMinute);
                }
            }, hour, minute, true);
            mTimePicker.show();
        });
        switchGeneral.setOnCheckedChangeListener((buttonView, isChecked) -> {
            notification.setActive(isChecked);
            updateNotification();

            if (!isChecked) {
                Intent intent = new Intent(Notifications.this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            } else {
                String[] timeArray = notification.getTime().split(":");
                startAlarm(this, Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]));
            }
        });

        switchDefinition.setOnCheckedChangeListener((buttonView, isChecked) -> {
            notification.setDefinition(isChecked);
            updateNotification();
        });

        LinearLayout preview = findViewById(R.id.previewNotification);
        preview.setOnClickListener(v -> {
            String json_1 = sharedPrefs.getString("WordOfDayDefinition", "");
            Type type_1 = new TypeToken<Definition>(){}.getType();
            Definition wordOfDayDefinition = gson.fromJson(json_1, type_1);

            String json_2 = sharedPrefs.getString("WordOfDay", "");
            Type type_2 = new TypeToken<WordOfDay>(){}.getType();
            WordOfDay wordOfDay = gson.fromJson(json_2, type_2);

            String content = "";
            if (notification.isDefinition()) {
                content = Jsoup.parse(wordOfDayDefinition.getHtmlRep()).text();
            }

            Helper.showNotification(this, wordOfDay.getWord(), content);
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    public static void startAlarm(Context context, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void updateNotification() {
        String notification_ = gson.toJson(notification);
        editor.putString("notification", notification_);
        editor.apply();
    }
}
