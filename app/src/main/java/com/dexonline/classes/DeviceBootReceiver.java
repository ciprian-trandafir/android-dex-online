package com.dexonline.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.dexonline.activities.Notifications;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("notification", "");
            Type type = new TypeToken<Notification>(){}.getType();
            Notification notification = gson.fromJson(json, type);
            String[] timeArray = notification.getTime().split(":");
            Notifications.startAlarm(context, Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]));
        }
    }
}
