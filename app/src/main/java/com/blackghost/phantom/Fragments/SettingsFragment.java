package com.blackghost.phantom.Fragments;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import com.blackghost.phantom.R;
import com.blackghost.phantom.Managers.SettingsManager;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SettingsManager settingsManager;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Apply the custom theme to this fragment
        getContext().getTheme().applyStyle(R.style.PreferenceScreen, true);
    }
}