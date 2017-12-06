package com.example.apps_miocali_project.control;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apps_miocali_project.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.example.apps_miocali_project.model.Destino;
import com.example.apps_miocali_project.model.Viaje;

/**
 * Created by Juan K on 29/11/2017.
 */

public class PViajeActivity extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    FloatingActionButton fabParadas, fabRecargas, fabWifi;
    boolean paradas, recargas, wifi;
    private Viaje viaje;
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
    private String x1,x2,y1,y2;
    private ConexionHTTPTReal darBusesTiempoReal;
    private Toolbar mToolbar;
    private HashMap<Marker,Destino> marcadoresPlanearRuta;
    private RelativeLayout paradasLayout;
    private Destino destino;
    private String nomParada;
    private TextView txtParadaNombre;
    private Button btnParada;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pviaje);
        Intent intent = getIntent();
        viaje = (Viaje) intent.getSerializableExtra("viaje");
        x1 = intent.getStringExtra("x1");
        x2 = intent.getStringExtra("x2");
        y1 = intent.getStringExtra("y1");
        y2 = intent.getStringExtra("y2");
        marcadoresPlanearRuta= new HashMap<Marker,Destino>();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ubicacionActivada = true;
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapViaje);
        mapFragment.getMapAsync(this);
        destino= null;
        nomParada="";
        txtParadaNombre=(TextView)findViewById(R.id.txtParadaNombre);
        btnParada=(Button)findViewById(R.id.btnParada);
        paradasLayout=(RelativeLayout)findViewById(R.id.paradaLayout);

    }

    public Activity getActivity() {
        return this;
    }

      public void onMapReady(GoogleMap googleMap) {
          googleMap.setMapStyle(
                  MapStyleOptions.loadRawResourceStyle(
                          this, R.raw.style_json));
        map = googleMap;
       // requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 1);
          map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
              @Override
              public boolean onMarkerClick(Marker marker) {
                  if(marcadoresPlanearRuta.containsKey(marker)){
                      Log.d("ALEJOTAG",marker.getTitle());
                      destino=marcadoresPlanearRuta.get(marker);
                      nomParada=marker.getTitle();
                      txtParadaNombre.setText(destino.getNombreDestino()+": "+ destino.getIdentBus());
                      paradasLayout.setVisibility(View.VISIBLE);
                  }
                  return false;
              }
          });

          map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
              @Override
              public void onMapClick(LatLng latLng) {
                  if (paradasLayout.getVisibility() == View.VISIBLE) {
                      paradasLayout.setVisibility(View.GONE);
                      txtParadaNombre.setText("");
                  }
              }});

          planearViaje(x1,y1,x2,y2);
    }

    public void planearViaje(String x1, String y1, String x2, String y2){
        for (int i = 0;i<viaje.getDestinos().size();i++){
            Destino dest = viaje.getDestinos().get(i);
            if(i==0){
                MarkerOptions marker_onclick = new MarkerOptions()
                        .anchor(0.5f, 0.5f) // Anchors the marker on the center
                        .title(dest.getNombreDestino())
                        .snippet("Hora de salida: " + viaje.getHoraSalida())
                        .position(new LatLng(dest.getLatitudDestino(), dest.getLongitudDestino())).icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
                Marker marker = map.addMarker(marker_onclick);
                marcadoresPlanearRuta.put(marker,dest);
            }else if(i == viaje.getDestinos().size()-1){
                MarkerOptions marker_onclick = new MarkerOptions()
                        .anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag)) // Anchors the marker on the center
                        .title(dest.getNombreDestino())
                        .snippet("Hora de llegada: " + viaje.getHoraLlegada())
                        .position(new LatLng(dest.getLatitudDestino(), dest.getLongitudDestino()));
                Marker marker = map.addMarker(marker_onclick);
                marcadoresPlanearRuta.put(marker,dest);
            }else if(i>0 && i < viaje.getDestinos().size()){
                if((i+1)<viaje.getDestinos().size()) {
                    if(!viaje.getDestinos().get(i).getIdentBus().equals(viaje.getDestinos().get(i+1).getIdentBus())) {
                        Destino d2=viaje.getDestinos().get(i+1);
                        MarkerOptions marker_onclick = new MarkerOptions()
                                .anchor(0.5f, 0.5f) // Anchors the marker on the center
                                .title(dest.getNombreDestino()+": "+d2.getIdentBus())
                                .snippet("Hora de llegada: " + viaje.getDestinos().get(i).getTiempoLlegada() + " Hora de salida: " +viaje.getDestinos().get(i+1).getTiempoSalida())
                                .position(new LatLng(dest.getLatitudDestino(), dest.getLongitudDestino())).icon(BitmapDescriptorFactory.fromResource(R.drawable.parada_map));

                        Marker marker = map.addMarker(marker_onclick);
                        marcadoresPlanearRuta.put(marker,dest);
                    }
                }
            }
            if((i+1)<viaje.getDestinos().size()){
                if(dest.getIdentBus().charAt(0) == 'A'){
                    Polyline line = map.addPolyline(new PolylineOptions()
                            .add(new LatLng(viaje.getDestinos().get(i).getLatitudDestino(),viaje.getDestinos().get(i).getLongitudDestino()), new LatLng(viaje.getDestinos().get(i+1).getLatitudDestino(),viaje.getDestinos().get(i+1).getLongitudDestino()))
                            .width(10)
                            .color(getResources().getColor(R.color.colorAccent)));
                }else if(dest.getIdentBus().charAt(0) == 'P'){
                    Polyline line = map.addPolyline(new PolylineOptions()
                            .add(new LatLng(viaje.getDestinos().get(i).getLatitudDestino(),viaje.getDestinos().get(i).getLongitudDestino()), new LatLng(viaje.getDestinos().get(i+1).getLatitudDestino(),viaje.getDestinos().get(i+1).getLongitudDestino()))
                            .width(10)
                            .color(Color.BLUE));
                }else if(dest.getIdentBus().charAt(0) == 'E'){
                    Polyline line = map.addPolyline(new PolylineOptions()
                            .add(new LatLng(viaje.getDestinos().get(i).getLatitudDestino(),viaje.getDestinos().get(i).getLongitudDestino()), new LatLng(viaje.getDestinos().get(i+1).getLatitudDestino(),viaje.getDestinos().get(i+1).getLongitudDestino()))
                            .width(10)
                            .color(Color.rgb(218, 214, 0)));
                }else if(dest.getIdentBus().charAt(0) == 'T'){
                    Polyline line = map.addPolyline(new PolylineOptions()
                            .add(new LatLng(viaje.getDestinos().get(i).getLatitudDestino(),viaje.getDestinos().get(i).getLongitudDestino()), new LatLng(viaje.getDestinos().get(i+1).getLatitudDestino(),viaje.getDestinos().get(i+1).getLongitudDestino()))
                            .width(10)
                            .color(Color.rgb(216, 35, 42)));
                }else{
                    //Defining a Dash of lengt 50 pixels
                    Dash myDASH = new Dash(15);
                    Gap myGAP = new Gap(8);
                    List<PatternItem> PATTERN_DASHED = Arrays.asList(myDASH,myGAP);
                    Polyline line = map.addPolyline(new PolylineOptions()
                            .add(new LatLng(viaje.getDestinos().get(i).getLatitudDestino(),viaje.getDestinos().get(i).getLongitudDestino()), new LatLng(viaje.getDestinos().get(i+1).getLatitudDestino(),viaje.getDestinos().get(i+1).getLongitudDestino()))
                            .width(10)
                            .color(Color.rgb(158, 158, 158)));
                    line.setPattern(PATTERN_DASHED);
                }

            }
        }
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(Double.parseDouble(y1),Double.parseDouble(x1)), DEFAULT_ZOOM));

    }

    public void mostrarParada(View v){
        //TODO
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
