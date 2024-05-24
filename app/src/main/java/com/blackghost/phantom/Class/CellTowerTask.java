package com.blackghost.phantom.Class;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
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

    @Override
    protected JSONObject doInBackground(String... params) {
        String urlString = params[0]; // URL для запиту
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResponse = "";

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
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
        if (result != null) {
            Log.d(TAG, "JSON Response: " + result.toString());
        }
    }

    public static void fetchCellTowers(String bbox) {
        String baseUrl = "https://opencellid.org/ajax/getCells.php?bbox=";
        String url = baseUrl + bbox;
        new CellTowerTask().execute(url);
    }
}
