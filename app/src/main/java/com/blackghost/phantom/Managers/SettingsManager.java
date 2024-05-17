package com.blackghost.phantom.Managers;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
public class SettingsManager {
    private SharedPreferences sharedPreferences;
    public SettingsManager(Context context){
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public boolean getBoolSettings(String key, boolean defaultValue){
        return sharedPreferences.getBoolean(key, defaultValue);
    }
    public void setBoolSettings(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
}