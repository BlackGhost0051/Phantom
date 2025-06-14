package com.blackghost.phantom.Managers;

import android.app.NotificationChannel;
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

import com.blackghost.phantom.Class.CellTowerTask;
import com.blackghost.phantom.Class.SearchCellTowerTask;
import com.blackghost.phantom.Interfaces.CellTowerInterface;
import com.blackghost.phantom.Interfaces.SearchCellTowerInterface;
import com.blackghost.phantom.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneStateManager extends BroadcastReceiver implements SearchCellTowerInterface, CellTowerInterface {
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


            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (androidx.core.content.ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
            if (cellLocation != null) {
                int cellId = cellLocation.getCid();
                int lac = cellLocation.getLac();
                String networkOperator = telephonyManager.getNetworkOperator();
                int mcc = Integer.parseInt(networkOperator.substring(0, 3));
                int mnc = Integer.parseInt(networkOperator.substring(3));

                String message = "CellID: " + cellId + ", LAC: " + lac + ", MCC: " + mcc + ", MNC: " + mnc;
                showNotification(context, "Call Started", message);
            }





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

        try {
            String lon = result.get("lon").toString();
            String lat = result.get("lat").toString();


            float lonF = (float) (Float.parseFloat(lon) - 0.0001);
            float latF = (float) (Float.parseFloat(lat) - 0.0001);

            float lonS = (float) (Float.parseFloat(lon) + 0.0001);
            float latS = (float) (Float.parseFloat(lat) + 0.0001);

            // -122.084 37.421997
            String bbox = String.valueOf(lonF) + "," + String.valueOf(latF) + "," + String.valueOf(lonS) + "," + String.valueOf(latS);
            Log.d("BOX",bbox);
            // https://opencellid.org/ajax/getCells.php?
            // bbox=-122.0841,37.421898,-122.0839,37.422096

            cellTowersTask(bbox);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }


    private void cellTowersTask(String bbox){
        String url = "https://opencellid.org/ajax/getCells.php?bbox=" + bbox;
        Log.d("URL", url);
        CellTowerTask cellTowerTask = new CellTowerTask("https://opencellid.org/ajax/getCells.php?bbox=", this::onTaskCompleted);
        cellTowerTask.execute(bbox);
    }
    @Override
    public void onTaskCompleted(JSONObject result) {
        Log.d("TASK", result.toString());
    }



    private void showNotification(Context context, String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "phantom_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // For Android 8+ (Oreo) create notification channel
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("phantom_channel", "Phantom Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(1, builder.build());
    }

}