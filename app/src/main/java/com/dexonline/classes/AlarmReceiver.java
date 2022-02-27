package com.dexonline.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import java.lang.reflect.Type;

public class AlarmReceiver extends BroadcastReceiver {
    private Notification notification;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();

        String json = sharedPrefs.getString("notification", "");
        Type type = new TypeToken<Notification>(){}.getType();
        notification = gson.fromJson(json, type);

        if (Helper.isNetwork(context) && notification.isActive()) {
            getWordOfDay(context);
        }
    }

    private void getWordOfDay(Context context) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://dexonline.ro/cuvantul-zilei/json", null, response -> {
            try {
                JSONObject requestedRecord = response.getJSONObject("requested").getJSONObject("record");
                String wordOfDay = requestedRecord.getString("word"),
                        definition = requestedRecord.getJSONObject("definition").getString("htmlRep");
                String content = "";
                if (notification.isDefinition()) {
                    content = Jsoup.parse(definition).text();
                }

                Helper.showNotification(context, wordOfDay, content);
            } catch (Exception e) {
                Helper.writeError(new com.dexonline.classes.Error("try-catch", e.toString(), "getWordOfDay()"), context);
            }
        }, error -> Helper.writeError(new com.dexonline.classes.Error("request", error.toString(), "getWordOfDay()"), context));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }
}
