package com.example.apps_miocali_project.control;

import com.getbase.floatingactionbutton.FloatingActionButton;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.apps_miocali_project.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    SupportMapFragment mapFragment;
    FloatingActionButton fabParadas, fabRecargas, fabWifi;
    boolean paradas,recargas,wifi;
    private DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        db= new DataBase(this);
        fabParadas=(FloatingActionButton) findViewById(R.id.accion_paradas);
        paradas=false;
        fabRecargas=(FloatingActionButton) findViewById(R.id.accion_recargas);
        recargas=false;
        fabWifi=(FloatingActionButton) findViewById(R.id.accion_wifi);
        wifi=false;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney, Australia, and move the camera.

        LatLng sydney = new LatLng(3.39948, -76.53147);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Home").icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_wifi_map)));
       long lat=db.getMundo().getEstacionesWifi().get(0).getLatitud();
        long lng=db.getMundo().getEstacionesWifi().get(0).getLongitud();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat ,lng),16));
        googleMap.addMarker(new MarkerOptions()
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(3.367050, -76.529009)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_wifi_map)));
        googleMap.addMarker(new MarkerOptions()
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(3.372651, -76.539931)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_wifi_map)));
        googleMap.addMarker(new MarkerOptions()
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(3.377107, -76.542731)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_wifi_map)));
        googleMap.addMarker(new MarkerOptions()
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(3.388118, -76.544928)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_wifi_map)));
        googleMap.addMarker(new MarkerOptions()
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(3.392862, -76.545754)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_wifi_map)));
    }

    public void puntosParadas(View v){
        //TODO
        if(!paradas){
            fabParadas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            paradas=true;

        }else{
            fabParadas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorDisabled), PorterDuff.Mode.MULTIPLY);
            paradas=false;
        }
    }


    public void puntosRecarga(View v){
        //TODO
        if(!recargas){
            fabRecargas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            recargas=true;
        }else{
            fabRecargas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorDisabled), PorterDuff.Mode.MULTIPLY);
            recargas=false;
        }
    }


    public void puntosWifi(View v){
        //TODO
        if(!wifi){
            fabWifi.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            wifi=true;
        }else{
            fabWifi.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorDisabled), PorterDuff.Mode.MULTIPLY);
            wifi=false;
        }
    }
}
