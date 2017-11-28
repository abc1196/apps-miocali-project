package com.example.apps_miocali_project.control;

   /* USAR PLANEAR VIAJE
   pg = ProgressDialog.show(this,"Por favor espera...", "Estamos planeando tu viaje.",false, false);
   tareaAsyncPlanearViaje planearViaje = new tareaAsyncPlanearViaje(pg, latitudInicio, longitudInicio, latitudFinal , longitudFinal );
   planearViaje.execute();

   pg = ProgressDialog.show(this,"Por favor espera...", "Estamos planeando tu viaje.",false, false);
   tareaAsyncPlanearViaje planearViaje = new tareaAsyncPlanearViaje(pg, "3.341629", " -76.530383", "3.445393" , "-76.563856" );
   planearViaje.execute();
   */


import com.getbase.floatingactionbutton.FloatingActionButton;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    SupportMapFragment mapFragment;
    FloatingActionButton fabParadas, fabRecargas, fabWifi;
    boolean paradas,recargas,wifi;
    private DataBase db;
    private GoogleMap map;

    private ProgressDialog pg;

    private ArrayList<Marker> listParadas;
    private ArrayList<Marker> listRecargas;
    private ArrayList<Marker> listWifi;
    private ConexionHTTPTReal darBusesTiempoReal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        db= new DataBase(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fabParadas=(FloatingActionButton) findViewById(R.id.accion_paradas);
        paradas=false;
        listParadas= new ArrayList<Marker>();
        fabRecargas=(FloatingActionButton) findViewById(R.id.accion_recargas);
        recargas=false;
        listRecargas= new ArrayList<Marker>();
        fabWifi=(FloatingActionButton) findViewById(R.id.accion_wifi);
        wifi=false;
        listWifi= new ArrayList<Marker>();
        darBusesTiempoReal = new ConexionHTTPTReal();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
    }

    public void puntosParadas(View v){
        //TODO
        if(!paradas){
            db.cargarModeloParadas();

            for (int i = 0; i<10;i++) {
                Double lat = db.getMundo().getParadasDelSistema().get(i).getLatitud();
                Double lng = db.getMundo().getParadasDelSistema().get(i).getLongitud();
                MarkerOptions marker_onclick =  new MarkerOptions()
                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                        .position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_ic_paradas));
                Marker marker=map.addMarker(marker_onclick);
                listParadas.add(marker);
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
            ConexionHTTPTReal sacarBuses = new ConexionHTTPTReal();
            sacarBuses.start();
            while(sacarBuses.isAlive()){

            }
            for(int i=0;i<sacarBuses.getBuses().size();i++){
                Log.d("ruta", sacarBuses.getBuses().get(i).getRouteId()+"");
                Log.d("lati", sacarBuses.getBuses().get(i).getLatitud()+"");
                Log.d("longi", sacarBuses.getBuses().get(i).getLongitud()+"");
                Log.d("stop", sacarBuses.getBuses().get(i).getStopId()+"");
            }
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
