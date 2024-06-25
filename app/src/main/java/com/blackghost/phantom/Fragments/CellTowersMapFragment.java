package com.blackghost.phantom.Fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

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
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class CellTowersMapFragment extends Fragment implements CellTowerInterface {

    private MapView mMap;
    private IMapController controller;
    private MyLocationNewOverlay mMyLocationOverlay;
    private RotationGestureOverlay rotationGestureOverlay;
    private CompassOverlay compassOverlay;
    private GeoPoint lastCenter = null;
    private SharedPreferences sharedPreferences;
    public CellTowersMapFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cell_towers_map, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        boolean savePositionBool = sharedPreferences.getBoolean("save_last_position_map", true);
        String startPosition;

        if(savePositionBool){
            startPosition = getLastPosition();
        } else {
            startPosition = sharedPreferences.getString("start_position", "51.509865, -0.118092");
        }

        String[] latLong = startPosition.split(",");
        double startLat;
        double startLon;

        try {
            startLat = Double.parseDouble(latLong[0]);
            startLon = Double.parseDouble(latLong[1]);
        } catch (Exception e){
            startLat = 51.509865;
            startLon = -0.118092;
        }

        mMap = view.findViewById(R.id.osmmap);

        mMap.setMultiTouchControls(true);

        Configuration.getInstance().setUserAgentValue("Phantom/0.0");

        controller = mMap.getController();

        controller.setZoom(getLastZoom());
        controller.setCenter(new GeoPoint(startLat, startLon));

        compassOverlay = new CompassOverlay(getActivity(), mMap);
        compassOverlay.enableCompass();
        mMap.getOverlays().add(compassOverlay);

        rotationGestureOverlay = new RotationGestureOverlay(mMap);
        rotationGestureOverlay.setEnabled(true);
        mMap.getOverlayManager().add(rotationGestureOverlay);

        mMap.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                double zoomLevel = mMap.getZoomLevelDouble();

                updateCompass();
                if (zoomLevel > 16.0) {
                    int width = mMap.getWidth();
                    int height = mMap.getHeight();

                    GeoPoint center = (GeoPoint) mMap.getMapCenter();

                    if(lastCenter != null){
                        double distance = calculateDistance(center,lastCenter);
                        if(distance >= 4.0 / zoomLevel) {
                            GeoPoint southWest = (GeoPoint) mMap.getProjection().fromPixels(0, height);
                            GeoPoint northEast = (GeoPoint) mMap.getProjection().fromPixels(width, 0);

                            double southWestLatitude = southWest.getLatitude();
                            double southWestLongitude = southWest.getLongitude();
                            double northEastLatitude = northEast.getLatitude();
                            double northEastLongitude = northEast.getLongitude();

                            String bbox = southWestLongitude + "," + southWestLatitude + "," + northEastLongitude + "," + northEastLatitude;
                            cellTowersTask(bbox);

                            lastCenter = center;
                        }
                    } else {
                            lastCenter = center;
                    }
                } else {
                    //clearMarkers();
                }
                return true;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                return true;
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveLastPosition();
    }

    private double calculateDistance(GeoPoint point1, GeoPoint point2){
        final int R = 6371;
        double latDistance = Math.toRadians(point2.getLatitude() - point1.getLatitude());
        double lonDistance = Math.toRadians(point2.getLongitude() - point1.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(point1.getLatitude())) * Math.cos(Math.toRadians(point2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance;
    }

    @Override
    public void onTaskCompleted(JSONObject result) {
        try {
            clearMarkers();
            JSONArray features = result.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject geometry = feature.getJSONObject("geometry");
                JSONObject properties = feature.getJSONObject("properties");

                JSONArray coordinates = geometry.getJSONArray("coordinates");
                double longitude = coordinates.getDouble(0);
                double latitude = coordinates.getDouble(1);

                String radio = properties.getString("radio");
                String mcc = properties.getString("mcc");
                String net = properties.getString("net");
                String cell = properties.getString("cell");
                String area = properties.getString("area");
                int samples = properties.getInt("samples");
                int range = properties.getInt("range");
                long created = properties.getLong("created");
                long updated = properties.getLong("updated");

                GeoPoint markerPoint = new GeoPoint(latitude, longitude);
                Marker marker = new Marker(mMap);

                Drawable icon;

                if (radio.equals("UMTS")){
                    icon = getResources().getDrawable(R.drawable.umts_icon);
                } else if(radio.equals("GSM")){
                    icon = getResources().getDrawable(R.drawable.gsm_icon);
                } else if(radio.equals("LTE")){
                    icon = getResources().getDrawable(R.drawable.lte_icon);
                } else {
                    icon = getResources().getDrawable(R.drawable.ic_launcher_foreground);
                }

                marker.setIcon(icon);
                marker.setPosition(markerPoint);
                mMap.getOverlays().add(marker);

                marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker, MapView mapView) {
                        String info = "Radio: " + radio + "\n" +
                                "MCC: " + mcc + "\n" +
                                "Net: " + net + "\n" +
                                "Cell: " + cell + "\n" +
                                "Area: " + area + "\n" +
                                "Samples: " + samples + "\n" +
                                "Range: " + range + "\n" +
                                "Created: " + created + "\n" +
                                "Updated: " + updated;
                        showIconInfoAlert(info);
                        return true;
                    }
                });
            }
        } catch (JSONException e) {
            Log.e("JSON", "Error parsing JSON"/*, e*/);
        }
    }

    private void cellTowersTask(String bbox){
        String url = "https://opencellid.org/ajax/getCells.php?bbox=" + bbox;
        CellTowerTask cellTowerTask = new CellTowerTask("https://opencellid.org/ajax/getCells.php?bbox=", this::onTaskCompleted);
        cellTowerTask.execute(bbox);
    }

    private void showIconInfoAlert(String info){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Marker Information")
                .setMessage(info)
                .setPositiveButton("OK", null)
                .create()
                .show();
    }

    private void clearMarkers() {
        mMap.getOverlays().clear();
        mMap.invalidate();
    }

    private void saveLastPosition(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        GeoPoint center = (GeoPoint) mMap.getMapCenter();
        editor.putString("last_position", center.getLatitude() + "," + center.getLongitude());
        editor.putString("last_zoom_level", String.valueOf(mMap.getZoomLevelDouble()));
        editor.apply();

    }

    private String getLastPosition(){
        String startPosition = sharedPreferences.getString("last_position", "51.509865, -0.118092");
        return  startPosition;
    }

    private double getLastZoom(){
        double lastZoom = Double.parseDouble(sharedPreferences.getString("last_zoom_level", "10.0"));
        return lastZoom;
    }

    private void updateCompass(){
        compassOverlay.setAzimuthOffset(-mMap.getMapOrientation());
        mMap.invalidate();
    }
}