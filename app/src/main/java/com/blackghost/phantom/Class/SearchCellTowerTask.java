package com.blackghost.phantom.Class;

import android.os.AsyncTask;

import com.blackghost.phantom.Interfaces.SearchCellTowerInterface;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null){
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = bufferedReader.readLine()) != null){
                buffer.append(line).append("\n");
            }

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
