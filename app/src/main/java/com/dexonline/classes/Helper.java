package com.dexonline.classes;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dexonline.R;
import com.dexonline.activities.AppLoading;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Helper {
    public static boolean isNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void writeError(com.dexonline.classes.Error error, Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = sharedPrefs.getString("errors", "");
        Type type = new TypeToken<List<com.dexonline.classes.Error>>(){}.getType();
        List<com.dexonline.classes.Error> errorList = gson.fromJson(json, type);

        if (errorList == null) {
            errorList = new ArrayList<>();
        }

        errorList.add(error);

        String errorList_ = gson.toJson(errorList);
        editor.putString("errors", errorList_);
        editor.apply();
    }

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "DexOnline Notification";
            String description = "Word of day";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("dex-online-notification", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void showNotification(Context context, String wordOfDay, String definition) {
        Helper.createNotificationChannel(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "dex-online-notification")
                .setSmallIcon(R.drawable.logo_round)
                .setContentTitle("Cuvântul zilei - " + wordOfDay)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(definition))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, AppLoading.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(100, builder.build());
    }
}
