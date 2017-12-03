package com.example.apps_miocali_project.control;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.example.apps_miocali_project.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Modelo.Bus;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, PlaceSelectionListener, View.OnClickListener  {
    private ProgressDialog pg;

    android.support.design.widget.FloatingActionButton fabUbicacion;
    SupportMapFragment mapFragment;
    FloatingActionMenu fabMenu;
    FloatingActionButton fabParadas, fabRecargas, fabWifi;
    boolean paradas, recargas, wifi;
    private DataBase db;
    private GoogleMap map;
    private float distanciaRutas;
    private HashMap<Marker, String> mapParadas;
    private PlaceAutocompleteFragment autocompleteFragment;
    private HashMap<Marker, String> mapRecargas;
    private ArrayList<Marker> listWifi;
    private ArrayList<Marker> busesTiempoReal;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private Location ultimaLocacion;
    private ArrayList<Marker> marcadoresPlanearRuta;
    private Button btnBusquedaDestino;


    private final static String KEY_LOCATION_LATITUD = "location latitud";
    private final static String KEY_LOCATION_LONGITUD = "location longitud";
    private final static String PARADAS_ACTIVAS="PARADAS";
    private final static String RECARGAS_ACTIVAS="RECARGAS";
    private final static String WIFI_ACTIVAS="WIFI";
    private boolean permisoPosicion;
    private boolean ubicacionActivada;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private static final float DEFAULT_ZOOM = 13;
    private static final double DEFAULT_LATITUD = 3.448972;
    private static final double DEFAULT_LONGITUD = -76.556218;
    private Toolbar mToolbar;

    private double distanciaFiltro;
    private ConexionHTTPTReal darBusesTiempoReal;
    private RelativeLayout paradasLayout, destinoLayout;
    private String idParada;
    private String nomParada;
    private TextView txtParadaNombre, txtDestinoNombre, txtDestinoDireccion;
    private Button btnParada, btnFav;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        darBusesTiempoReal = new ConexionHTTPTReal();
        idParada="";
        nomParada="";
        txtParadaNombre=(TextView)findViewById(R.id.txtParadaNombre);
        btnParada=(Button)findViewById(R.id.btnParada);
        btnFav=(Button)findViewById(R.id.btnFav);
        distanciaRutas=500;
        distanciaFiltro=500;
        busesTiempoReal = new ArrayList<>();
        db= new DataBase(this);
        db.cargarModeloRutas();
        //ubicacionActivada = true;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        fabMenu=(FloatingActionMenu)findViewById(R.id.menu_fab);
        fabMenu.setIconAnimated(false);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        destinoLayout=(RelativeLayout)findViewById(R.id.destinoLayout);
        txtDestinoNombre=(TextView)findViewById(R.id.txtDestinoNombre);
        txtDestinoDireccion=(TextView)findViewById(R.id.txtDestinoDireccion);
        paradasLayout=(RelativeLayout)findViewById(R.id.paradaLayout);
        fabUbicacion=(android.support.design.widget.FloatingActionButton)findViewById(R.id.fabUbicacion);
        fabParadas=(FloatingActionButton) findViewById(R.id.accion_paradas);
        paradas=false;
        mapParadas= new HashMap<Marker, String>();
        fabRecargas=(FloatingActionButton) findViewById(R.id.accion_recargas);
        recargas=false;
        mapRecargas= new HashMap<Marker, String>();
        fabWifi=(FloatingActionButton) findViewById(R.id.accion_wifi);
        wifi=false;
        listWifi= new ArrayList<Marker>();

        marcadoresPlanearRuta= new ArrayList<Marker>();
        permisoPosicion = true;
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("CO")
                .build();
        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setFilter(autocompleteFilter);
        autocompleteFragment.setHint("Busca tu destino!");
        autocompleteFragment.setBoundsBias(new LatLngBounds(new LatLng(3.336139, -76.671709),new LatLng(3.519145, -76.447176)));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                if(marcadoresPlanearRuta.isEmpty()) {
                    LatLng p = place.getLatLng();
                    Marker marker = map.addMarker(new MarkerOptions().position(p).title(place.getName().toString()).snippet(place.getAddress().toString()));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(p,16 ));
                    marcadoresPlanearRuta.add(marker);
                    txtDestinoNombre.setText(place.getName().toString());
                    txtDestinoDireccion.setText(place.getAddress().toString());
                    destinoLayout.setVisibility(View.VISIBLE);
                    Log.i("PLACETAG", "Place: " + place.getName());
                }else{
                    Toast.makeText(getApplicationContext(),"Solo puedes seleccionar un destino",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("PLACETAG", "An error occurred: " + status);
            }
        });

        autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(marcadoresPlanearRuta.size()>0){
                    Marker marker=marcadoresPlanearRuta.get(0);
                    marker.remove();
                    destinoLayout.setVisibility(View.GONE);
                    txtDestinoNombre.setText("");
                    txtDestinoDireccion.setText("");
                    autocompleteFragment.setText("");
                    marcadoresPlanearRuta.clear();
                }
            }
        });


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
        boolean p=sharedPref.getBoolean(PARADAS_ACTIVAS,false);
        boolean r=sharedPref.getBoolean(RECARGAS_ACTIVAS,false);
        boolean w=sharedPref.getBoolean(WIFI_ACTIVAS,false);
        if(p){pintarPuntosParadas();paradas=p;  fabParadas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        }
        if(r){pintarPuntosRecarga();recargas=r;  fabRecargas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        }
        if(w){pintarPuntosWifi();wifi=w;  fabWifi.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        }
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filtro:
                activarMenuFiltro();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));
        map = googleMap;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(DEFAULT_LATITUD, DEFAULT_LONGITUD), DEFAULT_ZOOM));
       requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 1);
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(mapParadas.containsKey(marker)){
                    Log.d("ALEJOTAG",marker.getTitle());
                    idParada=mapParadas.get(marker);
                    nomParada=marker.getTitle();
                    txtParadaNombre.setText(marker.getTitle());
                    if(btnParada.getVisibility()==View.GONE){
                        btnParada.setVisibility(View.VISIBLE);
                    }
                    if(btnFav.getVisibility()==View.GONE){
                        btnFav.setVisibility(View.VISIBLE);
                    }
                    paradasLayout.setVisibility(View.VISIBLE);
                } else if(mapRecargas.containsKey(marker)){
                    txtParadaNombre.setText(marker.getTitle());
                    if(btnParada.getVisibility()==View.VISIBLE){
                        btnParada.setVisibility(View.GONE);
                    }
                    if(btnFav.getVisibility()==View.VISIBLE){
                        btnFav.setVisibility(View.GONE);
                    }
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
                    txtParadaNombre.setText("");

                }
            }
        });
    }

    public void agregarAWidget(View view) {

    //TODO

    }

    public void puntosParadas(View v) {
        if(permisoPosicion&&!paradas){
        pintarPuntosParadas();
            fabParadas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            paradas = true;
        }else{
            borrarPuntosParada();
            fabParadas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorDisabled), PorterDuff.Mode.MULTIPLY);
            paradas = false;
        }
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PARADAS_ACTIVAS, paradas);

    }


    public void mostrarParada(View v){

        Intent intent= new Intent(getActivity(),ParadaActivity.class);
        intent.putExtra("parada",idParada);
        intent.putExtra("nombreParada",nomParada);
        startActivity(intent);
    }

    public void pintarPuntosParadas() {
            db.cargarModeloParadas();
            for (int i = 0; i < db.getMundo().getParadasDelSistema().size(); i++) {
                Double lat = db.getMundo().getParadasDelSistema().get(i).getLatitud();
                Double lng = db.getMundo().getParadasDelSistema().get(i).getLongitud();
                if (inRange(lat, lng, distanciaFiltro)) {
                    String id=db.getMundo().getParadasDelSistema().get(i).getId();
                    String nombre=db.getMundo().getParadasDelSistema().get(i).getNombre();
                    MarkerOptions marker_onclick = new MarkerOptions()
                            .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                            .position(new LatLng(lat, lng)).title(nombre).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_ic_paradas));
                    Marker marker=map.addMarker(marker_onclick);
                    mapParadas.put(marker,id);
                }
            }

    }

    public void borrarPuntosParada(){
        for(Map.Entry<Marker,String> entry: mapParadas.entrySet()){
            Marker marker= entry.getKey();
            marker.remove();
        }
        mapParadas.clear();
    }


    public void puntosRecarga(View v) {
        if(permisoPosicion&&!recargas) {
            pintarPuntosRecarga();
            fabRecargas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            recargas = true;
        }else{
            borrarPuntosRecarga();
            fabRecargas.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorDisabled), PorterDuff.Mode.MULTIPLY);
            recargas = false;
        }
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(RECARGAS_ACTIVAS, recargas);


    }

    public void pintarPuntosRecarga() {
            db.cargarModeloPuntosRecarga();
            for (int i = 0; i < db.getMundo().getPuntosRecarga().size(); i++) {
                Double lat = db.getMundo().getPuntosRecarga().get(i).getLatitud();
                Double lng = db.getMundo().getPuntosRecarga().get(i).getLongitud();
                String nombre=db.getMundo().getPuntosRecarga().get(i).getNombre();
                if (inRange(lat, lng, distanciaFiltro)) {
                    MarkerOptions marker_onclick = new MarkerOptions()
                            .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                            .position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_ic_recargas)).title(nombre);
                    Marker marker = map.addMarker(marker_onclick);
                    mapRecargas.put(marker,nombre);
                }
            }

    }

    public void borrarPuntosRecarga(){
        for(Map.Entry<Marker,String> entry: mapRecargas.entrySet()){
            Marker marker= entry.getKey();
            marker.remove();

        }
        mapRecargas.clear();
    }


    public void puntosWifi(View v) {
        if(permisoPosicion&&!wifi) {
            pintarPuntosWifi();
            fabWifi.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            wifi = true;
        }else{
            borrarPuntosWifi();
            fabWifi.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorDisabled), PorterDuff.Mode.MULTIPLY);
            wifi = false;
        }
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(WIFI_ACTIVAS, wifi);

    }

    public void pintarPuntosWifi() {
            db.cargarModeloPuntosWifi();
            for (int i = 0; i < db.getMundo().getEstacionesWifi().size(); i++) {
                Double lat = db.getMundo().getEstacionesWifi().get(i).getLatitud();
                Double lng = db.getMundo().getEstacionesWifi().get(i).getLongitud();
                    MarkerOptions marker_onclick = new MarkerOptions()
                            .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                            .position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_ic_wifi));
                    Marker marker = map.addMarker(marker_onclick);
                    listWifi.add(marker);
                }


    }

    public void borrarPuntosWifi(){
        for (int i = 0; i < listWifi.size(); i++) {
            Marker marker = listWifi.get(i);
            marker.remove();
        }
        listWifi.clear();
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
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    public void activarMenuBuses(View v){
       tareaAsyncBuscarRutaParada tr = new tareaAsyncBuscarRutaParada("", "P10A");
       tr.execute();
       Log.d("BUSEST", "Ejecutando tarea");
    }

    public void activarMenuFiltro(){
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
        seekbar1.setMinValue(100).setMaxValue(1000);
        Log.d("TAGDISTANCIA","DISTANCIA: "+(float)distanciaFiltro);
        if(distanciaFiltro==100){seekbar1.setMinStartValue(0 ).apply();}else{
        seekbar1.setMinStartValue((float) distanciaFiltro ).apply();}
     Log.d("TAGDISTANCIA: ", "SEEKBAR: "+  seekbar1.getMinStartValue());
        seekbar1.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                Log.d("TAGDISTANCIA","ACTIVITY: "+value.doubleValue());
                distanciaFiltro=value.doubleValue();
                txtDistanciaPuntos.setText(String.valueOf(value) + " m");
            }
        });
        seekbar.setMinStartValue((float)distanciaRutas).apply();
        seekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                distanciaRutas=value.floatValue();
                txtDistancia.setText(String.valueOf(value) + "m");
            }
        });

        builder.setView(view);
        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(paradas){
                            borrarPuntosParada();
                            pintarPuntosParadas();
                        }
                        if(wifi){
                            borrarPuntosWifi();
                            pintarPuntosWifi();
                        }
                        if(recargas){
                            borrarPuntosRecarga();
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

    @Override
    public void onClick(View view) {

    }
    @Override
    public void onPlaceSelected(Place place) {
            LatLng sel = place.getLatLng();
            double lat = sel.latitude;
            double lng = sel.longitude;
            String nom = place.getName().toString();
            String dir = place.getAddress().toString();
            Log.d("Lugares", lat+"");
        Log.d("Lugares", lng+"");
        Log.d("Lugares", nom+"");
        Log.d("Lugares", dir+"");

    }
    @Override
    public void onError(Status status) {

    }

    public void planearViaje(View v){
        if(!marcadoresPlanearRuta.isEmpty()){
            pg = ProgressDialog.show(this,"Por favor espera...", "Estamos planeando tu viaje",false, false);

            new tareaAsyncPlanearViaje(pg,Double.toString(ultimaLocacion.getLatitude()),Double.toString(ultimaLocacion.getLongitude()), Double.toString(marcadoresPlanearRuta.get(0).getPosition().latitude), Double.toString(marcadoresPlanearRuta.get(0).getPosition().longitude)).execute();
        }
    }

    public void pintarBuses(ArrayList<Bus> buses, String ruta,String identRuta){
        for (int i = 0; i < busesTiempoReal.size(); i++) {
            Marker marker = busesTiempoReal.get(i);
            marker.remove();
        }
        busesTiempoReal.clear();
        for(int i=0; i<buses.size(); i++) {
            if (buses.get(i).getRouteId().equals(ruta)) {
                Log.d("buses", buses.get(i).getLatitud()+"");
                Log.d("buses", buses.get(i).getLongitud()+"");
                MarkerOptions marker_onclick = new MarkerOptions()
                        .anchor(0f, 0.5f) // Anchors the marker on the center parte inferior
                        .title(identRuta)
                        .position(new LatLng(buses.get(i).getLatitud(), buses.get(i).getLongitud())).icon(BitmapDescriptorFactory.fromResource(R.drawable.bus32));
                Marker marker = map.addMarker(marker_onclick);
                busesTiempoReal.add(marker);
            }
        }
        darBusesTiempoReal.setConsultaLista(false);
        if(darBusesTiempoReal.isAlive() && darBusesTiempoReal.isMantener()){
            tareaAsyncBuscarRutaParada tr = new tareaAsyncBuscarRutaParada(ruta,  identRuta);
            tr.execute();
        }
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
            Intent intent = new Intent(getApplicationContext(), PViajeActivity.class);
            intent.putExtra("viaje", db.getMundo().getViaje() );
            intent.putExtra("x1", x1);
            intent.putExtra("x2", x2);
            intent.putExtra("y1", y1);
            intent.putExtra("y2", y2);
            startActivity(intent);
            pg.dismiss();
        }
    }
    public class tareaAsyncBuscarRutaParada extends AsyncTask<Void, Void, Void> {

        private String ruta;
        private String idRoute;
        private ArrayList<Bus> buses;
        public tareaAsyncBuscarRutaParada(String idRoute, String identRuta) {
            this.idRoute = identRuta;
            if(darBusesTiempoReal.isAlive()){
                ruta = idRoute;
            }else{
                ruta = "";
            }
            buses = new ArrayList<>();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Log.d("buses", "Entrando al hilo");
                if(!darBusesTiempoReal.isAlive()){
                    Log.d("buses", "Entrando al hilo por primera vez");
                    darBusesTiempoReal.start();
                    darBusesTiempoReal.setMantener(true);
                    boolean encontro = false;
                    for (int i = 0; i < db.getMundo().getRutas().size() && !encontro; i++) {
                        if (idRoute.equals(db.getMundo().getRutas().get(i).getIdentificador())) {
                            ruta = db.getMundo().getRutas().get(i).getIdRuta();
                            encontro = true;
                        }
                    }
                }
                while(!darBusesTiempoReal.isConsultaLista()){

                }
                Log.d("buses", ruta);
                Log.d("Buses", "Saliendo del hilo");
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected  void onPostExecute(Void voids){
            super.onPostExecute(voids);
            //while (darBusesTiempoReal.isMantener()){
                    ArrayList<Bus> buses = darBusesTiempoReal.getBuses();
                    pintarBuses(buses, ruta, idRoute);


            //}
        }
    }

}

