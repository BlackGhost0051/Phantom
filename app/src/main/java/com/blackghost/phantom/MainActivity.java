package com.blackghost.phantom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.blackghost.phantom.Fragments.MainFragment;
import com.blackghost.phantom.Fragments.SettingsFragment;
import com.blackghost.phantom.Managers.DataBaseManager;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));

        fragmentR(new MainFragment());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.settings) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    toolbar.setTitle("Settings");
                    fragmentR(new SettingsFragment());
                    return true;
                }
                return false;
            }
        });

        /*DataBaseManager dataBaseManager = new DataBaseManager(this);
        dataBaseManager.createDatabase("test1.db");
        dataBaseManager.createDatabase("test2.db");
        dataBaseManager.createDatabase("test3.db");

        if (dataBaseManager.createDatabase("test.db")) {
            Log.d("Test", "Database created");

            List<String> databaseNames = dataBaseManager.getDatabaseNames();
            Log.d("DataBase List", databaseNames.toString());
        } else {
            Log.e("Test", "Failed to create database");
        }*/

    }

    private void fragmentR(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}