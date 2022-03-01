package com.dexonline.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
}
