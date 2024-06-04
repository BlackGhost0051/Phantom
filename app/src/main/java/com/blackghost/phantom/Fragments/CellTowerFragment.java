package com.blackghost.phantom.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blackghost.phantom.R;

import java.util.List;

public class CellTowerFragment extends Fragment {

    TextView info_cell_tower;

    public CellTowerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cell_tower, container, false);

        info_cell_tower = view.findViewById(R.id.info_cell_tower);
        getCellTowerInfo();

        return view;
    }


    private void getCellTowerInfo() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(getContext().TELEPHONY_SERVICE);
    }
}