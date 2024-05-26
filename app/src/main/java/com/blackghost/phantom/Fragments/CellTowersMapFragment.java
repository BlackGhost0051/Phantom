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

import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
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
        controller.setCenter(new GeoPoint(51.5074, -0.1278));

        RotationGestureOverlay rotationGestureOverlay = new RotationGestureOverlay(mMap);
        rotationGestureOverlay.setEnabled(true);
        mMap.getOverlayManager().add(rotationGestureOverlay);

        String bbox = "20.978136062622074,50.01786707355468,20.984798669815067,50.02117598342286";
        cellTowersTask(bbox);
        cellTowersTask(bbox);
        cellTowersTask(bbox);
        cellTowersTask(bbox);
        cellTowersTask(bbox);

        return view;
    }

    @Override
    public void onTaskCompleted(JSONObject result) {
        Log.d("JSON", result.toString());
    }

    private void cellTowersTask(String bbox){
        CellTowerTask cellTowerTask = new CellTowerTask("https://opencellid.org/ajax/getCells.php?bbox=", this::onTaskCompleted);
        cellTowerTask.execute(bbox);
    }

}