package com.blackghost.phantom.Managers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.app.Notification;
import android.app.NotificationManager;
import androidx.core.app.NotificationCompat;
import com.blackghost.phantom.R;
public class PhoneStateManager extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (state == null) {
            return;
        }

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.d("PhoneStateReceiver", "Incoming call from: " + incomingNumber);
            showNotification(context, "Incoming call from: " + incomingNumber);
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

    private void showNotification(Context context, String message) {
        Log.d("Notification", message);
    }
}