package com.blackghost.phantom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.blackghost.phantom.Managers.DataBaseManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataBaseManager dataBaseManager = new DataBaseManager(this);

        if (dataBaseManager.createDatabase("test.db")) {
            Log.d("Test", "Database created");

            List<String> databaseNames = dataBaseManager.getDatabaseNames();
            Log.d("DataBase List", databaseNames.toString());
        } else {
            Log.e("Test", "Failed to create database");
        }

    }
}