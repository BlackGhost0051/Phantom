package com.blackghost.phantom.Class;

import android.os.AsyncTask;

import com.blackghost.phantom.Interfaces.SearchCellTowerInterface;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchCellTowerTask extends AsyncTask <String, Void , JSONObject> {
    private final String baseUrl;
    private final OnTaskCompleted listener;

    public SearchCellTowerTask(String baseUrl, OnTaskCompleted listener){
        this.baseUrl = baseUrl;
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        String task = params[0];
        String urlString = baseUrl + task;

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String jsonResponse = "";

        try{
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

        } catch (Exception e){

        }

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
