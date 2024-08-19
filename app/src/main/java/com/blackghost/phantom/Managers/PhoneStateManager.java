package com.blackghost.phantom.Managers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.app.Notification;
import android.app.NotificationManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.blackghost.phantom.Class.SearchCellTowerTask;
import com.blackghost.phantom.Interfaces.SearchCellTowerInterface;
import com.blackghost.phantom.R;

import org.json.JSONObject;

public class PhoneStateManager extends BroadcastReceiver implements SearchCellTowerInterface {
    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (state == null) {
            return;
        }

        // https://www.opencellid.org/ajax/searchCell.php?mcc=310&mnc=120&lac=21264&cell_id=175545858

        // return lon lan | make GET box
        // in box find more info for tower
        searchCellTask(context);

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.d("PhoneStateReceiver", "Incoming call from: " + incomingNumber);
        } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
            // Call started
            Log.d("PhoneStateReceiver", "Call started");
        } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
            // Call ended
            Log.d("PhoneStateReceiver", "Call ended");
        } else {
            Log.d("PhoneStateReceiver", "Unknown state: " + state);
        }
    }

    private String makeTask(String mcc, String mnc, String lac,String cell_id){
        return "/ajax/searchCell.php?mcc=" + mcc + "&mnc=" + mnc +"&lac=" + lac + "&cell_id=" + cell_id;
    }
    private void searchCellTask(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (telephonyManager == null) {
            Log.e("PhoneStateManager", "TelephonyManager is null");
            return;
        }

        if (androidx.core.content.ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("PhoneStateManager", "Location permission not granted");
            return;
        }

        GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
        if (cellLocation == null) {
            Log.e("PhoneStateManager", "CellLocation is null");
            return;
        }

        int cellId = cellLocation.getCid();
        int lac = cellLocation.getLac();
        String networkOperator = telephonyManager.getNetworkOperator();
        int mcc = Integer.parseInt(networkOperator.substring(0, 3));
        int mnc = Integer.parseInt(networkOperator.substring(3));

        SearchCellTowerTask searchCellTowerTask = new SearchCellTowerTask("https://www.opencellid.org", this::onSearchTaskCompleted);
        String Task = makeTask(String.valueOf(mcc), String.valueOf(mnc), String.valueOf(lac), String.valueOf(cellId));

        Log.d("TASK", Task);
        Toast.makeText(context, Task, Toast.LENGTH_LONG).show();
        searchCellTowerTask.execute(Task);
    }

    @Override
    public void onSearchTaskCompleted(JSONObject result) {
        Log.d("SearchTask", String.valueOf(result));
    }
}