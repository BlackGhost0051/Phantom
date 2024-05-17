package com.blackghost.phantom.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackghost.phantom.Managers.SettingsManager;
import com.blackghost.phantom.R;

import java.io.File;

public class SettingsFragment extends Fragment {

    private SettingsManager settingsManager;
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsManager = new SettingsManager(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        boolean test = settingsManager.getBoolSettings("local_data_base", false);

        Log.d("local_data_base",Boolean.toString(test));
        return view;
    }
}