package com.blackghost.phantom.Fragments;

import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.telephony.TelephonyManager;
import android.util.Log;
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
        info_cell_tower.setText(getCellTowerInfo());

        return view;
    }

    private String getCellTowerInfo() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(getContext().TELEPHONY_SERVICE);

        if (telephonyManager != null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                try {
                    List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
                    if (cellInfoList != null && !cellInfoList.isEmpty()) {
                        StringBuilder info = new StringBuilder();
                        for (CellInfo cellInfo : cellInfoList) {
                            if (cellInfo instanceof CellInfoGsm) {
                                CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                                info.append("Radio: GSM\n")
                                        .append("MCC: ").append(cellIdentity.getMcc()).append("\n")
                                        .append("Net: ").append(cellIdentity.getMnc()).append("\n")
                                        .append("Cell: ").append(cellIdentity.getCid()).append("\n")
                                        .append("Area: ").append(cellIdentity.getLac()).append("\n\n");
                            } else if (cellInfo instanceof CellInfoLte) {
                                CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                                info.append("Radio: LTE\n")
                                        .append("MCC: ").append(cellIdentity.getMcc()).append("\n")
                                        .append("Net: ").append(cellIdentity.getMnc()).append("\n")
                                        .append("Cell: ").append(cellIdentity.getCi()).append("\n")
                                        .append("Area: ").append(cellIdentity.getTac()).append("\n\n");
                            } else if (cellInfo instanceof CellInfoWcdma) {
                                CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
                                info.append("Radio: WCDMA\n")
                                        .append("MCC: ").append(cellIdentity.getMcc()).append("\n")
                                        .append("Net: ").append(cellIdentity.getMnc()).append("\n")
                                        .append("Cell: ").append(cellIdentity.getCid()).append("\n")
                                        .append("Area: ").append(cellIdentity.getLac()).append("\n\n");
                            }
                        }
                        return info.toString();
                    } else {
                        Log.d("PHON", "No cell info available");
                        return "No cell info available";
                    }
                } catch (SecurityException e) {
                    Log.d("PHON", "SecurityException: " + e.getMessage());
                    return "Permission denied for accessing cell info.";
                }
            } else {
                Log.d("PHON", "Permission not granted");
                return "Permission not granted";
            }
        } else {
            Log.d("PHON", "TelephonyManager is null");
            return "TelephonyManager is null";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCellTowerInfo();
            } else {
                Log.d("PHON", "Permission denied");
            }
        }
    }
}