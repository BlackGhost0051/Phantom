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
        getActivity().setTheme(R.style.preferences_style);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContext().getTheme().applyStyle(R.style.PreferenceScreen, true);
    }
}