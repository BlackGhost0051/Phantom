package com.blackghost.phantom.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackghost.phantom.Class.CellTowerTask;
import com.blackghost.phantom.Interfaces.CellTowerInterface;
import com.blackghost.phantom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureDetector;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class CellTowersMapFragment extends Fragment implements CellTowerInterface {

    private MapView mMap;
    private IMapController controller;
    private MyLocationNewOverlay mMyLocationOverlay;
    public CellTowersMapFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cell_towers_map, container, false);

        mMap = view.findViewById(R.id.osmmap);

        mMap.setMultiTouchControls(true);

        Configuration.getInstance().setUserAgentValue("Phantom/0.0");

        controller = mMap.getController();

        controller.setZoom(10.0);
        controller.setCenter(new GeoPoint(51.509865, -0.118092));

        RotationGestureOverlay rotationGestureOverlay = new RotationGestureOverlay(mMap);
        rotationGestureOverlay.setEnabled(true);
        mMap.getOverlayManager().add(rotationGestureOverlay);

        String bbox = "20.978136062622074,50.01786707355468,20.984798669815067,50.02117598342286";
        mMap.post(new Runnable() {
            @Override
            public void run() {
                int width = mMap.getWidth();
                int height = mMap.getHeight();

                GeoPoint center = (GeoPoint) mMap.getMapCenter();
                GeoPoint topLeft = (GeoPoint) mMap.getProjection().fromPixels(0, 0);
                GeoPoint bottomRight = (GeoPoint) mMap.getProjection().fromPixels(width, height);

                double latitude = center.getLatitude();
                double longitude = center.getLongitude();

                double topLeftLatitude = topLeft.getLatitude();
                double topLeftLongitude = topLeft.getLongitude();
                double bottomRightLatitude = bottomRight.getLatitude();
                double bottomRightLongitude = bottomRight.getLongitude();



                Log.d("Lat", String.valueOf(latitude));
                Log.d("Lon", String.valueOf(longitude));

                Log.d("TopLeft Lat", String.valueOf(topLeftLatitude));
                Log.d("TopLeft Lon", String.valueOf(topLeftLongitude));
                Log.d("BottomRight Lat", String.valueOf(bottomRightLatitude));
                Log.d("BottomRight Lon", String.valueOf(bottomRightLongitude));
            }
        });

        cellTowersTask(bbox);
        /*cellTowersTask(bbox);
        cellTowersTask(bbox);
        cellTowersTask(bbox);
        cellTowersTask(bbox);*/

        // https://opencellid.org/#zoom=17&lat=50.017742&lon=20.98434
        // https://opencellid.org/ajax/getCells.php?bbox=20.98101139068604,50.016088440333085,20.987673997879032,50.01939747269533
        // bbox
        // is it a square?

        return view;
    }

    @Override
    public void onTaskCompleted(JSONObject result) {
        Log.d("JSON", result.toString());

        try {
            JSONArray features = result.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject geometry = feature.getJSONObject("geometry");
                JSONObject properties = feature.getJSONObject("properties");

                JSONArray coordinates = geometry.getJSONArray("coordinates");
                double longitude = coordinates.getDouble(0);
                double latitude = coordinates.getDouble(1);

                GeoPoint markerPoint = new GeoPoint(latitude, longitude);
                Marker marker = new Marker(mMap);
                marker.setPosition(markerPoint);

                mMap.getOverlays().add(marker);

                String radio = properties.getString("radio");
                String mcc = properties.getString("mcc");
                String net = properties.getString("net");
                String cell = properties.getString("cell");
                String area = properties.getString("area");
                int samples = properties.getInt("samples");
                int range = properties.getInt("range");
                long created = properties.getLong("created");
                long updated = properties.getLong("updated");

                Log.d("Feature " + i, "Coordinates: [" + longitude + ", " + latitude + "]");
                Log.d("Feature " + i, "Radio: " + radio + ", MCC: " + mcc + ", Net: " + net);
                Log.d("Feature " + i, "Cell: " + cell + ", Area: " + area);
                Log.d("Feature " + i, "Samples: " + samples + ", Range: " + range);
                Log.d("Feature " + i, "Created: " + created + ", Updated: " + updated);
            }
        } catch (JSONException e) {
            Log.e("JSON", "Error parsing JSON", e);
        }
    }

    private void cellTowersTask(String bbox){
        CellTowerTask cellTowerTask = new CellTowerTask("https://opencellid.org/ajax/getCells.php?bbox=", this::onTaskCompleted);
        cellTowerTask.execute(bbox);
    }

}