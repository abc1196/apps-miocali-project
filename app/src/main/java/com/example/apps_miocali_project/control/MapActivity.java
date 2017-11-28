package com.example.apps_miocali_project.control;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.os.AsyncTask;
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
    private ProgressDialog pg;
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


    private final static String KEY_LOCATION_LATITUD = "location latitud";
    private final static String KEY_LOCATION_LONGITUD = "location longitud";
    private boolean permisoPosicion;
    private boolean ubicacionActivada;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private static final float DEFAULT_ZOOM = 13;
    private static final double DEFAULT_LATITUD = 3.448972;
    private static final double DEFAULT_LONGITUD = -76.556218;

    private double distanciaFiltro;
    private ConexionHTTPTReal darBusesTiempoReal;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        db = new DataBase(this);

        ubicacionActivada = true;

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
        distanciaFiltro = 500;

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String latitud = sharedPref.getString(KEY_LOCATION_LATITUD, "0");
        String longitud = sharedPref.getString(KEY_LOCATION_LONGITUD, "0");
        if (latitud.equals("0")) {
            Log.d("1", "No hay shared");
        } else {
            Log.d("1", "Hay Shared");
            double sharedLatitud = Double.parseDouble(latitud);
            double sharedLongitud = Double.parseDouble(longitud);
            ultimaLocacion = new Location("");
            ultimaLocacion.setLatitude(sharedLatitud);
            ultimaLocacion.setLongitude(sharedLongitud);
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                ultimaLocacion = location;
                Log.d("loc", "El loc mananager cambio a " + location.getLatitude() + " " + location.getLongitude());
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(KEY_LOCATION_LATITUD, "" + ultimaLocacion.getLatitude());
                editor.putString(KEY_LOCATION_LONGITUD, "" + ultimaLocacion.getLongitude());
                editor.commit();
                Log.d("1", "Guardó en shared por movimiento");
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

    public Activity getActivity() {
        return this;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(DEFAULT_LATITUD, DEFAULT_LONGITUD), DEFAULT_ZOOM));
        requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    public void puntosParadas(View v) {
        if(permisoPosicion){
        pintarPuntosParadas();}
    }

    public void pintarPuntosParadas() {
        if (!paradas) {
            db.cargarModeloParadas();
            for (int i = 0; i < db.getMundo().getParadasDelSistema().size(); i++) {
                Double lat = db.getMundo().getParadasDelSistema().get(i).getLatitud();
                Double lng = db.getMundo().getParadasDelSistema().get(i).getLongitud();
                if (inRange(lat, lng, distanciaFiltro)) {
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
        if(permisoPosicion){
        pintarPuntosRecarga();}
    }

    public void pintarPuntosRecarga() {

        if (!recargas) {
            db.cargarModeloPuntosRecarga();
            for (int i = 0; i < db.getMundo().getPuntosRecarga().size(); i++) {
                Double lat = db.getMundo().getPuntosRecarga().get(i).getLatitud();
                Double lng = db.getMundo().getPuntosRecarga().get(i).getLongitud();
                if (inRange(lat, lng, distanciaFiltro)) {
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
        if(permisoPosicion) {
            pintarPuntosWifi();
        }
    }

    public void pintarPuntosWifi() {
        if (!wifi) {
            db.cargarModeloPuntosWifi();
            for (int i = 0; i < db.getMundo().getEstacionesWifi().size(); i++) {
                Double lat = db.getMundo().getEstacionesWifi().get(i).getLatitud();
                Double lng = db.getMundo().getEstacionesWifi().get(i).getLongitud();
                if (inRange(lat, lng, distanciaFiltro)) {
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
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
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
                    Toast.makeText(getApplicationContext(), "Por favor enciende ubicación actual.",
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 1);
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
    public void darUbicacionActual(View v) {
        if(ultimaLocacion==null){
            ultimaLocacion = new Location("");
            ultimaLocacion.setLatitude(DEFAULT_LATITUD);
            ultimaLocacion.setLongitude(DEFAULT_LONGITUD);
            Toast.makeText(getApplicationContext(), "Por favor acepta los permisos de ubicación",
                    Toast.LENGTH_LONG).show();
            actualizarLocalizaciónActual();
        }else {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(ultimaLocacion.getLatitude(), ultimaLocacion.getLongitude()), 16));
            updateLocationUI();
            if(ultimaLocacion.getLatitude()==DEFAULT_LATITUD && ultimaLocacion.getLongitude()==DEFAULT_LONGITUD){
                Toast.makeText(getApplicationContext(), "Por favor enciende tu ubicación",
                        Toast.LENGTH_LONG).show();
                actualizarLocalizaciónActual();
            }
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
    public class tareaAsyncPlanearViaje extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pg;
        private String x1,x2,y1,y2;

        public tareaAsyncPlanearViaje(ProgressDialog pg,String y1, String x1, String y2, String x2) {
            this.pg=pg;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                db.planearRuta(x1,y1,x2,y2);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected  void onPostExecute(Void voids){
            super.onPostExecute(voids);
            pg.dismiss();
        }
    }
    public class tareaAsyncBuscarRutaParada extends AsyncTask<Void, Void, Void> {



        public tareaAsyncBuscarRutaParada(String idParada, String idRoute) {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                darBusesTiempoReal.start();
                while (darBusesTiempoReal.isMantener()){
                    if(darBusesTiempoReal.isConsultaLista()){

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected  void onPostExecute(Void voids){
            super.onPostExecute(voids);
            pg.dismiss();
        }
    }
}

