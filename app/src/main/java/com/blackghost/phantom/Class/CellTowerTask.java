package com.blackghost.phantom.Class;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CellTowerTask extends AsyncTask<String, Void, JSONObject> {
    private static final String TAG = "CellTowerTask";
    private final String baseUrl;
    private final OnTaskCompleted listener;

    public CellTowerTask(String baseUrl, OnTaskCompleted listener) {
        this.baseUrl = baseUrl;
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        String bbox = params[0];
        String urlString = baseUrl + bbox;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResponse = "";

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            jsonResponse = buffer.toString();
        } catch (Exception e) {
            Log.e(TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return new JSONObject(jsonResponse);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
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
