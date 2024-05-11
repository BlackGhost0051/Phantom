package com.blackghost.phantom.Managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataBaseManager {
    private Context context;
    private List<String> databaseNames;

    public DataBaseManager(Context context) {
        this.context = context;
    }

    public boolean createDatabase(String databaseName) {
        try {
            String dbPath = context.getFilesDir().getPath() + File.separator + "databases" + File.separator + databaseName;
            File dbFile = new File(dbPath);

            if (dbFile.exists()) {
                return true;
            }

            File dbDir = new File(dbFile.getParent());
            dbDir.mkdirs();

            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getDatabaseNames() {
        databaseNames = new ArrayList<>();
        try {
            String dbDirPath = context.getFilesDir().getPath() + File.separator + "databases";
            File dbDir = new File(dbDirPath);

            File[] files = dbDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".db")) {
                        databaseNames.add(file.getName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseNames;
    }

}
