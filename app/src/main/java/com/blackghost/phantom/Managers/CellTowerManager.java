package com.blackghost.phantom.Managers;

import android.util.Log;

import com.blackghost.phantom.Class.CellTowerTask;
import com.blackghost.phantom.Interfaces.CellTowerInterface;

import org.json.JSONObject;

public class CellTowerManager implements CellTowerInterface {

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
