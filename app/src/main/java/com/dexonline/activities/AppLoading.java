package com.dexonline.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dexonline.R;
import com.dexonline.classes.Definition;
import com.dexonline.classes.WordOfDay;
import com.google.gson.Gson;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AppLoading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_loading);

        getWordOfDay();
    }

    public void onBackPressed() { }

    public void getWordOfDay()
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://dexonline.ro/cuvantul-zilei/json", null, response -> {
            try {
                JSONObject requestedRecord = response.getJSONObject("requested").getJSONObject("record");
                WordOfDay wordOfDay = new WordOfDay(
                        requestedRecord.getString("year"),
                        requestedRecord.getString("word"),
                        requestedRecord.getString("reason"),
                        requestedRecord.getString("image")
                );
                Definition definition = new Definition(
                        requestedRecord.getJSONObject("definition").getString("id"),
                        requestedRecord.getJSONObject("definition").getString("htmlRep"),
                        requestedRecord.getJSONObject("definition").getString("userNick"),
                        requestedRecord.getJSONObject("definition").getString("sourceName"),
                        requestedRecord.getJSONObject("definition").getString("createDate"),
                        requestedRecord.getJSONObject("definition").getString("modDate")
                );

                List<WordOfDay> listOthersWordOfDay = new ArrayList<>();
                JSONArray jsonRecordOthersRecord = new JSONArray(response.getJSONObject("others").getString("record"));
                for (int i = 0; i < jsonRecordOthersRecord.length(); i++) {
                    JSONObject wordOfDayObject = jsonRecordOthersRecord.getJSONObject(i);
                    listOthersWordOfDay.add(new WordOfDay(
                            wordOfDayObject.getString("year"),
                            wordOfDayObject.getString("word"),
                            wordOfDayObject.getString("reason"),
                            wordOfDayObject.getString("image")
                    ));
                }

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String listOthersWordOfDay_ = gson.toJson(listOthersWordOfDay);
                editor.putString("listOthersWordOfDay", listOthersWordOfDay_);  //save previous Word Of Day

                String definition_ = gson.toJson(definition);
                editor.putString("WordOfDayDefinition", definition_); //save current Word Of Day definition

                String wordOfDay_ = gson.toJson(wordOfDay);
                editor.putString("WordOfDay", wordOfDay_); //save current Word Of Day

                editor.apply();

                new Handler().postDelayed(() -> {
                    startActivity(new Intent(AppLoading.this, HomePage.class));
                    finish();
                }, 1000);
            } catch (Exception e) {
                Log.d("ERROR", e.toString());
            }
        }, error -> Log.d("ERROR REQUEST", error.toString()));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}

//de facut ceva atunci cand nu este internet
//de scris in fisier erorile