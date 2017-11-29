package com.example.apps_miocali_project.control;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.location.Criteria;
import android.location.Location;

import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apps_miocali_project.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    FloatingActionButton fabParadas, fabRecargas, fabWifi;
    boolean paradas, recargas, wifi;
    private DataBase db;
    private GoogleMap map;
    private Ubicacion ubicacion;
    private double distanciaFiltro;

    private ArrayList<Marker> listParadas;
    private ArrayList<Marker> listRecargas;
    private ArrayList<Marker> listWifi;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        db = new DataBase(this);

        ubicacion = new Ubicacion(this);

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
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        ubicacion.centrarMapaInicial(map);
    }

    public void puntosParadas(View v) {
        if(ubicacion.darPermisoPosicion()){
        pintarPuntosParadas();}
    }

    public void pintarPuntosParadas() {
        if (!paradas) {
            db.cargarModeloParadas();
            for (int i = 0; i < db.getMundo().getParadasDelSistema().size(); i++) {
                Double lat = db.getMundo().getParadasDelSistema().get(i).getLatitud();
                Double lng = db.getMundo().getParadasDelSistema().get(i).getLongitud();
                if (ubicacion.inRange(lat, lng, distanciaFiltro)) {
                    MarkerOptions marker_onclick = new MarkerOptions()
                            .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                            .position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_ic_paradas));
                    Marker marker = map.addMarker(marker_onclick);
                    listParadas.add(marker);
                }
            }
            fabParadas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            paradas = true;

        } else {
            for (int i = 0; i < listParadas.size(); i++) {
                Marker marker = listParadas.get(i);
                marker.remove();

            }
            listParadas.clear();
            fabParadas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorDisabled), PorterDuff.Mode.MULTIPLY);
            paradas = false;

        }
    }


    public void puntosRecarga(View v) {
        if(ubicacion.darPermisoPosicion()){
        pintarPuntosRecarga();}
    }

    public void pintarPuntosRecarga() {

        if (!recargas) {
            db.cargarModeloPuntosRecarga();
            for (int i = 0; i < db.getMundo().getPuntosRecarga().size(); i++) {
                Double lat = db.getMundo().getPuntosRecarga().get(i).getLatitud();
                Double lng = db.getMundo().getPuntosRecarga().get(i).getLongitud();
                if (ubicacion.inRange(lat, lng, distanciaFiltro)) {
                    MarkerOptions marker_onclick = new MarkerOptions()
                            .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                            .position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_ic_recargas));
                    Marker marker = map.addMarker(marker_onclick);
                    listRecargas.add(marker);
                }
            }
            fabRecargas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            recargas = true;
        } else {
            for (int i = 0; i < listRecargas.size(); i++) {
                Marker marker = listRecargas.get(i);
                marker.remove();

            }
            listRecargas.clear();
            fabRecargas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorDisabled), PorterDuff.Mode.MULTIPLY);
            recargas = false;
        }
    }


    public void puntosWifi(View v) {
        if(ubicacion.darPermisoPosicion()) {
            pintarPuntosWifi();
        }
    }

    public void pintarPuntosWifi() {
        if (!wifi) {
            db.cargarModeloPuntosWifi();
            for (int i = 0; i < db.getMundo().getEstacionesWifi().size(); i++) {
                Double lat = db.getMundo().getEstacionesWifi().get(i).getLatitud();
                Double lng = db.getMundo().getEstacionesWifi().get(i).getLongitud();
                if (ubicacion.inRange(lat, lng, distanciaFiltro)) {
                    MarkerOptions marker_onclick = new MarkerOptions()
                            .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                            .position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_ic_wifi));
                    Marker marker = map.addMarker(marker_onclick);
                    listWifi.add(marker);
                }
            }
            fabWifi.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            wifi = true;
        } else {
            for (int i = 0; i < listWifi.size(); i++) {
                Marker marker = listWifi.get(i);
                marker.remove();

            }
            listWifi.clear();
            fabWifi.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorDisabled), PorterDuff.Mode.MULTIPLY);
            wifi = false;
        }
    }

    public void activarMenuFiltro(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Ajustes de Filtro");
        builder.setMessage("Establece radio máximo");
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_filter_settings, null);
        final TextView txtDistancia= (TextView) view.findViewById(R.id.txtDistancia);
        final CrystalSeekbar seekbar=(CrystalSeekbar)view.findViewById(R.id.rangeSeekbar4);
        seekbar.setMinStartValue((float) distanciaFiltro ).apply();
        seekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                distanciaFiltro=value.floatValue();
                txtDistancia.setText(String.valueOf(value) + " m");
            }
        });
        builder.setView(view);
        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(paradas){
                            pintarPuntosParadas();
                        }
                        if(wifi){
                            pintarPuntosWifi();
                        }
                        if(recargas){
                            pintarPuntosRecarga();
                        }
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void darUbicacionActual(View v) {
        ubicacion.actualizarLocalizaciónActual();
        }
}
