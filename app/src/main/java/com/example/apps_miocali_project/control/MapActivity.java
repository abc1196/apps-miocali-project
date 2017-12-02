package com.example.apps_miocali_project.control;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.example.apps_miocali_project.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ProgressDialog pg;

    public SharedPreferences sharedPref;
    android.support.design.widget.FloatingActionButton fabUbicacion;
    SupportMapFragment mapFragment;
    FloatingActionButton fabParadas, fabRecargas, fabWifi;
    boolean paradas, recargas, wifi;
    private DataBase db;
    private GoogleMap map;
    private double distanciaFiltro;
    private float distanciaRutas;
    private HashMap<Marker, String> mapParadas;
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

    private static final float DEFAULT_ZOOM = 12;
    private static final double DEFAULT_LATITUD = 3.4375964;
    private static final double DEFAULT_LONGITUD = -76.5166973;
    private ConexionHTTPTReal darBusesTiempoReal;
    private RelativeLayout paradasLayout;
    private String idParada;
    private TextView txtRutaNombre;

    private Marker puntoUsuario;
    private boolean modoManual;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        idParada="";
        txtRutaNombre=(TextView)findViewById(R.id.txtRutaNombre);
        distanciaRutas=500;
        distanciaFiltro=500;
        db= new DataBase(this);
        ubicacionActivada = true;
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

        modoManual = false;
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

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
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
                if(!modoManual) {
                    Toast.makeText(getApplicationContext(), "Pasando a modo manual",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                ultimaLocacion.setLatitude(marker.getPosition().latitude);
                ultimaLocacion.setLongitude(marker.getPosition().longitude);
                guardarShared();
                puntoUsuario.setPosition(marker.getPosition());
                modoManual = true;
                if(paradas){
                    pintarPuntosParadas();
                }
                if(wifi){
                    pintarPuntosWifi();
                }
                if(recargas){
                    pintarPuntosRecarga();
                }
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

        });
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(!modoManual) {
                    ultimaLocacion = location;
                    puntoUsuario.setPosition(new LatLng(ultimaLocacion.getLatitude(),ultimaLocacion.getLongitude()));
                    puntoUsuario.setVisible(true);
                    Log.d("tag","posicion automatica cambio a "+ultimaLocacion.getLatitude() + ultimaLocacion.getLongitude());
                    guardarShared();
                }
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

        MarkerOptions puntoUsuarioOptions = new MarkerOptions()
                .anchor(0.0f, 0.0f) // Anchors the marker on the bottom left
                .position(new LatLng(DEFAULT_LATITUD, DEFAULT_LONGITUD)).draggable(true);

        puntoUsuario = map.addMarker(puntoUsuarioOptions);
        puntoUsuario.setVisible(false);
        if(cargarShared()){
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(ultimaLocacion.getLatitude(), ultimaLocacion.getLongitude()), DEFAULT_ZOOM));
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 1);
            puntoUsuario.setPosition(new LatLng(ultimaLocacion.getLatitude(),ultimaLocacion.getLongitude()));
            puntoUsuario.setVisible(true);
        }else{
            actualizarLocalizaciónActual();
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(DEFAULT_LATITUD, DEFAULT_LONGITUD), DEFAULT_ZOOM));
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 1);
        }
    }

    public void puntosParadas(View v) {
        if(permisoPosicion){
            pintarPuntosParadas();}
    }


    public void mostrarRutasParada(){

        Bundle bundle = new Bundle();
        //MARKER CON LA ID_RUTA
        String id_ruta="";
        ArrayList<String> rutas = db.cargarRutasParada(id_ruta);
        bundle.putStringArrayList("rutasParada",rutas);
        StropDetailFragment fragmento = new StropDetailFragment();
        fragmento.setArguments(bundle);

    }

    public void pintarPuntosParadas() {
        if (!paradas) {
            db.cargarModeloParadas();
            for (int i = 0; i < db.getMundo().getParadasDelSistema().size(); i++) {
                Double lat = db.getMundo().getParadasDelSistema().get(i).getLatitud();
                Double lng = db.getMundo().getParadasDelSistema().get(i).getLongitud();
                if (inRange(lat, lng, distanciaFiltro)) {
                    String id=db.getMundo().getParadasDelSistema().get(i).getId();
                    String nombre=db.getMundo().getParadasDelSistema().get(i).getNombre();
                    MarkerOptions marker_onclick = new MarkerOptions()
                            .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                            .position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_ic_paradas)).title(nombre);
                    Marker marker=map.addMarker(marker_onclick);
                    mapParadas.put(marker,id);
                    // listParadas.add(marker);
                }
            }
            fabParadas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            paradas = true;

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
                            .anchor(0.0f, 0.0f) // Anchors the marker on the bottom left
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
                    puntoUsuario.setVisible(true);
                    puntoUsuario.setPosition(new LatLng(ultimaLocacion.getLatitude(),ultimaLocacion.getLongitude()));
                    guardarShared();
                    Log.d("1", "Guardó en shared por tomar ubicación");
                } else {
                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                    String provider = locationManager.getBestProvider(criteria, false);
                    ultimaLocacion = locationManager.getLastKnownLocation(provider);
                    if(ultimaLocacion==null){
                        if(cargarShared()){
                            puntoUsuario.setVisible(true);
                            puntoUsuario.setPosition(new LatLng(ultimaLocacion.getLatitude(),ultimaLocacion.getLongitude()));
                            Toast.makeText(getApplicationContext(), "Por favor enciende ubicación actual",
                                    Toast.LENGTH_LONG).show();
                        }else {
                            ultimaLocacion = new Location("");
                            ultimaLocacion.setLatitude(DEFAULT_LATITUD);
                            ultimaLocacion.setLongitude(DEFAULT_LONGITUD);
                            puntoUsuario.setVisible(true);
                            puntoUsuario.setPosition(new LatLng(ultimaLocacion.getLatitude(),ultimaLocacion.getLongitude()));
                            Toast.makeText(getApplicationContext(), "Por favor enciende ubicación actual",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    ubicacionActivada =false;

                }
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                ultimaLocacion = null;
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
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, false);
        ultimaLocacion = locationManager.getLastKnownLocation(provider);
        if(ultimaLocacion==null){
            if(cargarShared()){
                puntoUsuario.setVisible(true);
                puntoUsuario.setPosition(new LatLng(ultimaLocacion.getLatitude(),ultimaLocacion.getLongitude()));
                Toast.makeText(getApplicationContext(), "Por favor enciende ubicación actual",
                        Toast.LENGTH_LONG).show();
            }else {
                ultimaLocacion = new Location("");
                ultimaLocacion.setLatitude(DEFAULT_LATITUD);
                ultimaLocacion.setLongitude(DEFAULT_LONGITUD);
                puntoUsuario.setVisible(true);
                puntoUsuario.setPosition(new LatLng(ultimaLocacion.getLatitude(),ultimaLocacion.getLongitude()));
                Toast.makeText(getApplicationContext(), "Por favor enciende ubicación actual",
                        Toast.LENGTH_LONG).show();
            }
        }else{
            puntoUsuario.setVisible(true);
            puntoUsuario.setPosition(new LatLng(ultimaLocacion.getLatitude(),ultimaLocacion.getLongitude()));
        }
    }
    public void darUbicacionActual(View v) {
        if(modoManual){
            Toast.makeText(getApplicationContext(), "Pasando a modo automático",
                    Toast.LENGTH_LONG).show();
        }
        modoManual = false;
        actualizarLocalizaciónActual();
        //if(ultimaLocacion==null){
          //  actualizarLocalizaciónActual();
        //}else {
          //  actualizarLocalizaciónActual();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
        new LatLng(ultimaLocacion.getLatitude(), ultimaLocacion.getLongitude()), 16));
            //updateLocationUI();
            //if(ultimaLocacion.getLatitude()==DEFAULT_LATITUD && ultimaLocacion.getLongitude()==DEFAULT_LONGITUD){
             //   actualizarLocalizaciónActual();
            //}
        //}
    }
    public boolean cargarShared(){
        boolean loaded = false;
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String latitud = sharedPref.getString(KEY_LOCATION_LATITUD, "0");
        String longitud = sharedPref.getString(KEY_LOCATION_LONGITUD, "0");
        if (latitud.equals("0")) {
            loaded = false;
        } else {
            loaded=true;
            double sharedLatitud = Double.parseDouble(latitud);
            double sharedLongitud = Double.parseDouble(longitud);
            ultimaLocacion = new Location("");
            ultimaLocacion.setLatitude(sharedLatitud);
            ultimaLocacion.setLongitude(sharedLongitud);
        }
        return loaded;
    }
    public void guardarShared(){
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_LOCATION_LATITUD, "" + ultimaLocacion.getLatitude());
        editor.putString(KEY_LOCATION_LONGITUD, "" + ultimaLocacion.getLongitude());
        editor.commit();
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
        seekbar.setMinStartValue((float) distanciaFiltro ).apply();
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