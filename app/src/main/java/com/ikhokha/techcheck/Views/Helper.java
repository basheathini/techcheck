package com.ikhokha.techcheck.Views;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.Map;

class Helper {
    static String getDate(){
        java.sql.Timestamp timestamp = new java.sql.Timestamp( new java.util.Date().getTime());
        return timestamp.toString();
    }
    static void saveHash(String key, Map<String, Map<String, Object>> hash, Activity activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(hash);
        editor.putString(key,json);
        editor.apply();
    }
    static Map<String, Map<String, Object>> getHash(String key, Activity activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        String json = prefs.getString(key,"");
        java.lang.reflect.Type type = new TypeToken< Map<String, Map<String, Object>>>(){}.getType();
        Map<String, Map<String, Object>> hash = gson.fromJson(json, type);
        return hash;
    }

    static void clearPref(String key, Activity activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear().apply();
    }
}
