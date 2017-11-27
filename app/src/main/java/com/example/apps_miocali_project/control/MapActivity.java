package com.example.apps_miocali_project.control;

import com.getbase.floatingactionbutton.FloatingActionButton;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.location.Location;

import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.apps_miocali_project.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.InputStream;
import java.util.ArrayList;

import static android.content.res.Resources.getSystem;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    FloatingActionButton fabParadas, fabRecargas, fabWifi;
    boolean paradas, recargas, wifi;
    private DataBase db;
    private GoogleMap map;


    private ArrayList<Marker> listParadas;
    private ArrayList<Marker> listRecargas;
    private ArrayList<Marker> listWifi;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private Location ultimaLocacion;


    private final static String KEY_LOCATION = "location";
    private final static String KEY_CAMERA_POSITION = "camera_position";
    private CameraPosition posicionCamara;
    private boolean permisoPosicion;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private static final float DEFAULT_ZOOM = 13;
    private static final double DEFAULT_LATITUD = 3.4307172;
    private static final double DEFAULT_LONGITUD = -76.5189694;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        db = new DataBase(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fabParadas = (FloatingActionButton) findViewById(R.id.accion_paradas);
        paradas = false;
        listParadas = new ArrayList<Marker>();
        fabRecargas = (FloatingActionButton) findViewById(R.id.accion_recargas);
        recargas = false;
        listRecargas = new ArrayList<Marker>();
        fabWifi = (FloatingActionButton) findViewById(R.id.accion_wifi);
        wifi = false;
        listWifi = new ArrayList<Marker>();

        if (savedInstanceState != null) {
            ultimaLocacion = savedInstanceState.getParcelable(KEY_LOCATION);
            posicionCamara = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                ultimaLocacion = location;
                Log.d("loc",location.getLatitude()+" "+location.getLongitude());
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(DEFAULT_LATITUD, DEFAULT_LONGITUD), DEFAULT_ZOOM));
        requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
    }

    public void puntosParadas(View v){
        //TODO
        if(!paradas){
            db.cargarModeloParadas();
            for (int i = 0; i<db.getMundo().getParadasDelSistema().size();i++) {
                Double lat = db.getMundo().getParadasDelSistema().get(i).getLatitud();
                Double lng = db.getMundo().getParadasDelSistema().get(i).getLongitud();
                if(inRange(lat,lng,1000)){
                MarkerOptions marker_onclick =  new MarkerOptions()
                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                        .position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_ic_paradas));
                Marker marker=map.addMarker(marker_onclick);
                listParadas.add(marker);}
            }
            fabParadas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            paradas=true;

        }else{
            for (int i = 0; i<listParadas.size();i++) {
                Marker marker= listParadas.get(i);
                marker.remove();

            }
            listParadas.clear();
            fabParadas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorDisabled), PorterDuff.Mode.MULTIPLY);
            paradas=false;

        }
    }


    public void puntosRecarga(View v){
        //TODO
        if(!recargas){
            db.cargarModeloPuntosRecarga();
            for (int i = 0; i<db.getMundo().getPuntosRecarga().size();i++) {
                Double lat = db.getMundo().getPuntosRecarga().get(i).getLatitud();
                Double lng = db.getMundo().getPuntosRecarga().get(i).getLongitud();
                MarkerOptions marker_onclick =  new MarkerOptions()
                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                        .position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_ic_recargas));
                Marker marker=map.addMarker(marker_onclick);
                listRecargas.add(marker);
            }
            fabRecargas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            recargas=true;
        }else{
            for (int i = 0; i<listRecargas.size();i++) {
                Marker marker= listRecargas.get(i);
                marker.remove();

            }
            listRecargas.clear();
            fabRecargas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorDisabled), PorterDuff.Mode.MULTIPLY);
            recargas=false;
        }
    }


    public void puntosWifi(View v){
        //TODO
        if(!wifi){
            db.cargarModeloPuntosWifi();
            for (int i = 0; i<db.getMundo().getEstacionesWifi().size();i++) {
                Double lat = db.getMundo().getEstacionesWifi().get(i).getLatitud();
                Double lng = db.getMundo().getEstacionesWifi().get(i).getLongitud();
                MarkerOptions marker_onclick =  new MarkerOptions()
                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                        .position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_ic_wifi));
                Marker marker=map.addMarker(marker_onclick);
                listWifi.add(marker);
            }
            fabWifi.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            wifi=true;
        }else{
            for (int i = 0; i<listWifi.size();i++) {
                Marker marker= listWifi.get(i);
                marker.remove();

            }
            listWifi.clear();
            fabWifi.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorDisabled), PorterDuff.Mode.MULTIPLY);
            wifi=false;
        }
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
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                ultimaLocacion = null;
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    public boolean inRange(double latitudPunto, double longitudPunto, double rango){
        boolean out = false;

        rango = rango/1000;

        double x = ultimaLocacion.getLatitude()-latitudPunto;
        double y = ultimaLocacion.getLongitude()-longitudPunto;

        double valor1 = (rango)/(111.319);

        double valor2 = Math.sqrt((x*x)+(y*y));

        if(valor2<valor1){
            out = true;
        }
        return out;
    }


}
