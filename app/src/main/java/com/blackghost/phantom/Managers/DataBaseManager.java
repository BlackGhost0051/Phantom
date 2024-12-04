package com.blackghost.phantom.Managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataBaseManager {
    /*
    * MAKE .csv
    GSM,	260,2, 	58703,	48771,	0, 	    21.624526977539,	51.449661254883, 	1000,	1,	    1,1451225006,1451225006,0
    type, 	mcc, 	mnc, 	lac, 	cell_id	lan     			lon			        range	samples
    * */



    private final String DATABASE_NAME = "my.db";

    private Context context;
    private List<String> databaseNames;

    public DataBaseManager(Context context) {
        this.context = context;
        initDataBase();
    }


    private void initDataBase(){
        createDatabase();
    }

    public boolean createDatabase() {
        try {
            String dbPath = context.getFilesDir().getPath() + File.separator + "databases" + File.separator + DATABASE_NAME;
            File dbFile = new File(dbPath);

            if (dbFile.exists()) {
                return true;
            }

            File dbDir = new File(dbFile.getParent());
            dbDir.mkdirs();

            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS cell_tower_base (act TEXT,mcc TEXT,mnc TEXT,lac TEXT,cellid TEXT)");
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
