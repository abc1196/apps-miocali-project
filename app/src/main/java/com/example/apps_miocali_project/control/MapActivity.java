package com.example.apps_miocali_project.control;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.apps_miocali_project.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    android.support.design.widget.FloatingActionButton fabUbicacion;
    FloatingActionButton fabParadas, fabRecargas, fabWifi;
    boolean paradas,recargas,wifi;
    private DataBase db;
    private GoogleMap map;
    private float distanciaFiltro, distanciaRutas;
    private HashMap<Marker, String> mapParadas;
    private ArrayList<Marker> listRecargas;
    private ArrayList<Marker> listWifi;
    private RelativeLayout paradasLayout;
    private String idParada;
    private TextView txtRutaNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        idParada="";
        txtRutaNombre=(TextView)findViewById(R.id.txtRutaNombre);
        distanciaRutas=500;
        distanciaFiltro=500;
        db= new DataBase(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        paradasLayout=(RelativeLayout)findViewById(R.id.paradaLayout);
        fabUbicacion=(android.support.design.widget.FloatingActionButton)findViewById(R.id.fabUbicacion);
        fabParadas=(FloatingActionButton) findViewById(R.id.accion_paradas);
        paradas=false;
        mapParadas= new HashMap<Marker, String>();
        fabRecargas=(FloatingActionButton) findViewById(R.id.accion_recargas);
        recargas=false;
        listRecargas= new ArrayList<Marker>();
        fabWifi=(FloatingActionButton) findViewById(R.id.accion_wifi);
        wifi=false;
        listWifi= new ArrayList<Marker>();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(mapParadas.containsKey(marker)){
                    Log.d("ALEJOTAG",marker.getTitle());
                    idParada=mapParadas.get(marker);
                    txtRutaNombre.setText(marker.getTitle());
                    paradasLayout.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(paradasLayout.getVisibility() == View.VISIBLE) {
                    paradasLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    public void puntosParadas(View v){
        //TODO
        if(!paradas){
            db.cargarModeloParadas();
            for (int i = 100; i<120;i++) {
                Double lat = db.getMundo().getParadasDelSistema().get(i).getLatitud();
                Double lng = db.getMundo().getParadasDelSistema().get(i).getLongitud();
                String id=db.getMundo().getParadasDelSistema().get(i).getId();
                String nombre=db.getMundo().getParadasDelSistema().get(i).getNombre();
                MarkerOptions marker_onclick =  new MarkerOptions()
                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                        .position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_ic_paradas)).title(nombre);
                Marker marker=map.addMarker(marker_onclick);
                mapParadas.put(marker,id);
               // listParadas.add(marker);
            }
            fabParadas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            paradas=true;

        }else{

            for(Map.Entry<Marker,String> entry: mapParadas.entrySet()){
                Marker marker= entry.getKey();
                marker.remove();
            }
            mapParadas.clear();
//            for (int i = 0; i<listParadas.size();i++) {
//                Marker marker= listParadas.get(i);
//                marker.remove();
//            }
//            listParadas.clear();
            fabParadas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorDisabled), PorterDuff.Mode.MULTIPLY);
            paradas=false;

        }
    }


    public void puntosRecarga(View v){
        //TODO
        if(!recargas){
            db.cargarModeloPuntosRecarga();
            for (int i = 0; i<10;i++) {
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
            for (int i = 0; i<10;i++) {
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

    public void darUbicacionActual(View v){

    }

    public void activarMenuFiltro(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Ajustes de Filtro");
        builder.setMessage("Establece radio máximo");
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_filter_settings, null);
        final TextView txtDistanciaPuntos=(TextView)view.findViewById(R.id.txtDistanciaPuntos);
        final TextView txtDistancia= (TextView) view.findViewById(R.id.txtDistancia);
        final CrystalSeekbar seekbar=(CrystalSeekbar)view.findViewById(R.id.rangeSeekbar4);
        final CrystalSeekbar seekbar1=(CrystalSeekbar)view.findViewById(R.id.rangeSeekbar6);
        seekbar.setMinStartValue(distanciaFiltro ).apply();
        seekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                distanciaFiltro=value.floatValue();
                txtDistancia.setText(String.valueOf(value) + " m");
            }
        });
        seekbar1.setMinStartValue(distanciaRutas).apply();
        seekbar1.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                distanciaRutas=value.floatValue();
                txtDistanciaPuntos.setText(String.valueOf(value) + "m");
            }
        });

        builder.setView(view);
        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO BENRU
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

    public void verRutasParada(View v){

    }



}
