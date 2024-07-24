package com.blackghost.phantom.Class;

import android.os.AsyncTask;

import com.blackghost.phantom.Interfaces.SearchCellTowerInterface;

import org.json.JSONObject;

public class SearchCellTowerTask extends AsyncTask <String, Void , JSONObject> {
    private final String baseUrl;
    private final OnTaskCompleted listener;

    public SearchCellTowerTask(String baseUrl, OnTaskCompleted listener){
        this.baseUrl = baseUrl;
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        String urlString = baseUrl;

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        if (listener != null) {
            listener.onTaskCompleted(result);
        }
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(JSONObject result);
    }
}
