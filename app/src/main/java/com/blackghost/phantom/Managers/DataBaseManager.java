package com.blackghost.phantom.Managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataBaseManager {
    private Context context;
    private List<String> databaseNames;
    private static String databaseDirectory = "/databases/";

    public DataBaseManager(Context context) {
        this.context = context;
        databaseNames = new ArrayList<>();
    }

    public boolean createDatabase(String databaseName) {
        String dbPath = context.getDatabasePath(databaseDirectory + File.separator + databaseName).getPath();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
        if (db != null) {
            db.close();
            return true;
        } else {
            return false;
        }
    }
    public List<String> getDatabaseNames() {
        return databaseNames;
    }
}
