package com.blackghost.phantom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blackghost.phantom.Fragments.CellTowerFragment;
import com.blackghost.phantom.Fragments.CellTowersMapFragment;
import com.blackghost.phantom.Fragments.MainFragment;
import com.blackghost.phantom.Fragments.SettingsFragment;
import com.blackghost.phantom.Managers.PhoneStateManager;
import com.google.android.material.navigation.NavigationView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    private BroadcastReceiver phoneStateReceiver;
    private TextView app_version_TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));

        fragmentR(new MainFragment());

        View header = navigationView.getHeaderView(0);
        app_version_TextView = header.findViewById(R.id.app_version);
        app_version_TextView.setText(BuildConfig.VERSION_NAME);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.cell_towers_map) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    toolbar.setTitle(R.string.toolbar_cell_towers_map);
                    fragmentR(new CellTowersMapFragment());
                    return true;
                }
                if (item.getItemId() == R.id.cell_tower) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    toolbar.setTitle(R.string.toolbar_cell_tower);
                    fragmentR(new CellTowerFragment());
                    return true;
                }
                if (item.getItemId() == R.id.settings) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    toolbar.setTitle(R.string.toolbar_settings);
                    fragmentR(new SettingsFragment());
                    return true;
                }
                return false;
            }
        });

        if (!hasPermissions(this)) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_CONTACTS,
            }, PERMISSIONS_REQUEST_CODE);
        } else {
            registerPhoneStateReceiver();
        }


        test_message();
    }

    private boolean hasPermissions(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    private void registerPhoneStateReceiver() {
        phoneStateReceiver = new PhoneStateManager();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        registerReceiver(phoneStateReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (phoneStateReceiver != null) {
            unregisterReceiver(phoneStateReceiver);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && allPermissionsGranted(grantResults)) {
                registerPhoneStateReceiver();
            } else {
                Toast.makeText(this, "Permissions are required for this app to function properly", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean allPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void fragmentR(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void test_message() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MyChannel";
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "CHANNEL_ID")
                .setSmallIcon(R.drawable.baseline_map_24)
                .setContentTitle("Test message")
                .setContentText("My message")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSIONS_REQUEST_CODE);
            }
        }
    }
}