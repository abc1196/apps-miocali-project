package com.example.apps_miocali_project.control;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by rubendario on 28/11/2017.
 */

public class Ubicacion {

    private static final float DEFAULT_ZOOM = 13;
    private static final double DEFAULT_LATITUD = 3.448972;
    private static final double DEFAULT_LONGITUD = -76.556218;

    private static final double DEFAULT_DISTANCIA = 500;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private final static String KEY_LOCATION_LATITUD = "location latitud";
    private final static String KEY_LOCATION_LONGITUD = "location longitud";

    private SharedPreferences sharedPref;

    private boolean permisoPosicion;
    private boolean ubicacionActivada;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private Location ultimaLocacion;

    private Activity mapActivity;
    private GoogleMap map;

    public Ubicacion(Activity mapActivity){
        this.mapActivity = mapActivity;
        sharedPref = mapActivity.getPreferences(Context.MODE_PRIVATE);
        loadShared1();
        locationManager = (LocationManager) mapActivity.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                ultimaLocacion = location;
                saveShared();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

    }

    public boolean loadShared1(){
        boolean loaded = false;
        String latitud = sharedPref.getString(KEY_LOCATION_LATITUD, "0");
        String longitud = sharedPref.getString(KEY_LOCATION_LONGITUD, "0");
        if (latitud.equals("0")) {
            ultimaLocacion = new Location("");
            ultimaLocacion.setLatitude(DEFAULT_LATITUD);
            ultimaLocacion.setLongitude(DEFAULT_LONGITUD);
        } else {
            loaded = true;
            double sharedLatitud = Double.parseDouble(latitud);
            double sharedLongitud = Double.parseDouble(longitud);
            ultimaLocacion = new Location("");
            ultimaLocacion.setLatitude(sharedLatitud);
            ultimaLocacion.setLongitude(sharedLongitud);
        }
        return loaded;
    }

    public void saveShared(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_LOCATION_LATITUD, "" + ultimaLocacion.getLatitude());
        editor.putString(KEY_LOCATION_LONGITUD, "" + ultimaLocacion.getLongitude());
        editor.commit();
    }

    public void centrarMapaInicial(GoogleMap map){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(DEFAULT_LATITUD, DEFAULT_LONGITUD), DEFAULT_ZOOM));
        mapActivity.requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 1);
        this.map = map;
    }

    public boolean darPermisoPosicion(){
        return permisoPosicion;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        permisoPosicion = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisoPosicion = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (permisoPosicion) {
                Log.d("1", "Tomó actual");
                map.getUiSettings().setMyLocationButtonEnabled(true);
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                if (ultimaLocacion != null) {
                    map.setMyLocationEnabled(true);
                    SharedPreferences sharedPref = mapActivity.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(KEY_LOCATION_LATITUD, "" + ultimaLocacion.getLatitude());
                    editor.putString(KEY_LOCATION_LONGITUD, "" + ultimaLocacion.getLongitude());
                    editor.commit();
                    Log.d("1", "Guardó en shared por tomar ubicación");
                } else {
                    ultimaLocacion = new Location("");
                    ultimaLocacion.setLatitude(DEFAULT_LATITUD);
                    ultimaLocacion.setLongitude(DEFAULT_LONGITUD);
                    ubicacionActivada =false;
                    Toast.makeText(mapActivity.getApplicationContext(), "Por favor enciende ubicación actual.",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                ultimaLocacion = null;
                Log.d("1", "Ultima locacion cambio a null");
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public boolean inRange(double latitudPunto, double longitudPunto, double rango) {
        boolean out = false;

        rango = rango / 1000;

        double x = ultimaLocacion.getLatitude() - latitudPunto;
        double y = ultimaLocacion.getLongitude() - longitudPunto;

        double valor1 = (rango) / (111.319);

        double valor2 = Math.sqrt((x * x) + (y * y));

        if (valor2 < valor1) {
            out = true;
        }
        return out;
    }



    public void actualizarLocalizaciónActual() {
        if (ActivityCompat.checkSelfPermission(mapActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mapActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mapActivity.requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 1);
            return;
        }
        map.setMyLocationEnabled(true);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, false);
        ultimaLocacion = locationManager.getLastKnownLocation(provider);
        if(ultimaLocacion==null){
            ultimaLocacion = new Location("");
            ultimaLocacion.setLatitude(DEFAULT_LATITUD);
            ultimaLocacion.setLongitude(DEFAULT_LONGITUD);
        }
    }
}


