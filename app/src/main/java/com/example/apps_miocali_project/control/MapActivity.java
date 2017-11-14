package com.example.apps_miocali_project.control;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.apps_miocali_project.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    SupportMapFragment mapFragment;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(3.39948, -76.53147);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Home"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(3.39948 ,-76.53147),16));
    }
}
