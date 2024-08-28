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
import android.widget.ImageView;
import android.widget.TextView;

import com.blackghost.phantom.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.os.Handler;


public class CellTowerFragment extends Fragment {

    private TextView info_cell_tower;
    private ImageView security_status_imageView;
    private TextView security_status_textView;

    private Handler handler = new Handler();
    private Runnable runnable;

    public CellTowerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cell_tower, container, false);

        info_cell_tower = view.findViewById(R.id.info_cell_tower);
        security_status_imageView = view.findViewById(R.id.security_status_imageView);
        security_status_textView = view.findViewById(R.id.security_status_textView);


        security_status_imageView.setImageResource(R.drawable.ic_launcher_background);
        security_status_textView.setText("STATUS");



        runnable = new Runnable() {
            @Override
            public void run() {
                info_cell_tower.setText(getCellTowerInfo());
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(runnable);

        return view;
    }

    private String getCellTowerInfo() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(getContext().TELEPHONY_SERVICE);

        if (telephonyManager != null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                try {
                    List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
                    if (cellInfoList != null && !cellInfoList.isEmpty()) {
                        String info = "";

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                        String currentTime = sdf.format(new Date());

                        for (CellInfo cellInfo : cellInfoList) {
                            if (cellInfo instanceof CellInfoGsm) {
                                CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                                int signalStrength = ((CellInfoGsm) cellInfo).getCellSignalStrength().getDbm();
                                info += "Radio: GSM\n" +
                                        "MCC: " + cellIdentity.getMcc() + "\n" +
                                        "Net: " + cellIdentity.getMnc() + "\n" +
                                        "Cell: " + cellIdentity.getCid() + "\n" +
                                        "Area: " + cellIdentity.getLac() + "\n" +
                                        "Signal Strength: " + signalStrength + " dBm\n" +
                                        "Update Time: " + currentTime + "\n\n";
                            } else if (cellInfo instanceof CellInfoLte) {
                                CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                                int signalStrength = ((CellInfoLte) cellInfo).getCellSignalStrength().getDbm();
                                info += "Radio: LTE\n" +
                                        "MCC: " + cellIdentity.getMcc() + "\n" +
                                        "Net: " + cellIdentity.getMnc() + "\n" +
                                        "Cell: " + cellIdentity.getCi() + "\n" +
                                        "Area: " + cellIdentity.getTac() + "\n" +
                                        "Signal Strength: " + signalStrength + " dBm\n" +
                                        "Update Time: " + currentTime + "\n\n";
                            } else if (cellInfo instanceof CellInfoWcdma) {
                                CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
                                int signalStrength = ((CellInfoWcdma) cellInfo).getCellSignalStrength().getDbm();
                                info += "Radio: WCDMA\n" +
                                        "MCC: " + cellIdentity.getMcc() + "\n" +
                                        "Net: " + cellIdentity.getMnc() + "\n" +
                                        "Cell: " + cellIdentity.getCid() + "\n" +
                                        "Area: " + cellIdentity.getLac() + "\n" +
                                        "Signal Strength: " + signalStrength + " dBm\n" +
                                        "Update Time: " + currentTime + "\n\n";
                            }
                        }
                        return info;
                    } else {
                        return "No cell info available"; // Android 11 error
                    }
                } catch (SecurityException e) {
                    Log.d("PHON", "SecurityException: " + e.getMessage());
                    return "Permission denied for accessing cell info.";
                }
            } else {
                return "Permission not granted";
            }
        } else {
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