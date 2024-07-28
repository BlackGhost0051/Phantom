package com.blackghost.phantom.Managers;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
public class SettingsManager {
    private SharedPreferences sharedPreferences;
    public SettingsManager(Context context){
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

}