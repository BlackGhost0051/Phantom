package com.blackghost.phantom.Managers;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.blackghost.phantom.Class.CellTowerTask;
import com.blackghost.phantom.Interfaces.CellTowerInterface;
import com.blackghost.phantom.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;

public class CellTowerManager implements CellTowerInterface {

    private File appFolder;

    public CellTowerManager(Context context){
        File root = Environment.getExternalStorageDirectory();
        String folderName = context.getString(R.string.app_name);
        appFolder = new File(root, folderName);

        if(!appFolder.exists()){
            boolean created = appFolder.mkdirs();

            if(created){

            } else {

            }
        } else {

        }
    }



    public void getCellIdInfo(){

    }



    private void getInfoFromAPI(){

    }
    private void getInfoFromTask(){

    }
    private void getInfoFromCsv(File file){
        try{

        }catch(Exception e) {
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
    public void onTaskCompleted(JSONObject result){

    }
}
