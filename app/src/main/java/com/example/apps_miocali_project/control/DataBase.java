package com.example.apps_miocali_project.control;

/**
 * Created by Juan K on 13/11/2017.
 */


import android.content.ContentValues;
import java.io.*;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.apps_miocali_project.R;

import java.util.ArrayList;




import org.json.*;

import Modelo.Parada;
import Modelo.PuntoMapa;
import Modelo.SistemaMio;


public class DataBase extends SQLiteOpenHelper{
    private SistemaMio mundo;

    private static final int DATABASE_VERSION = 1;
    private static final String BASE_PUNTOS_MAPA = "basePuntosMapa";
    public static final String TABLA_WIFI = "EstacionesWifi";
    public static final String TABLA_PUNTOS_RECARGA  = "puntosRecarga";
    public static final String TABLA_PARADAS = "paradas";


    private static final String PUNTO_RECARGA = "nombrePuntoRecarga";
    private static final String LATITUD_PUNTO_RECARGA = "latitudRecarga";
    private static final String LONGITUD_PUNTO_RECARGA = "longitudRecarga";

    private static final String PUNTO_WIFI = "nombrePuntoWifi";
    private static final String LATITUD_PUNTO_WIFI = "latitudWifi";
    private static final String LONGITUD_PUNTO_WIFI = "longitudWifi";

    private static final String PARADA = "nombreParada";
    private static final String LATITUD_PARADA = "latitudParada";
    private static final String LONGITUD_PARADA = "longitudParada";
    private static final String ID_PARADA = "idParada";




    public static final String SQL_CREATE_TABLA_WIFI = "CREATE TABLE "
            + DataBase.TABLA_WIFI + " ( "
            + LATITUD_PUNTO_WIFI + " TEXT PRIMARY KEY, "
            + LONGITUD_PUNTO_WIFI + " TEXT ) "
            ;

    public static final String SQL_CREATE_TABLA_RECARGA = "CREATE TABLE "
            + DataBase.TABLA_PUNTOS_RECARGA + " ( "
            + PUNTO_RECARGA + " TEXT PRIMARY KEY, "
            + LATITUD_PUNTO_RECARGA + " TEXT, "
            + LONGITUD_PUNTO_RECARGA + " TEXT ) "
            ;

    public static final String SQL_CREATE_TABLA_PARADA = "CREATE TABLE "
            + DataBase.TABLA_PARADAS + " ( "
            + ID_PARADA + " TEXT PRIMARY KEY, "
            + PARADA + " TEXT, "
            + LATITUD_PARADA + " TEXT, "
            + LONGITUD_PARADA + " TEXT ) "
            ;

    public DataBase(Context context){
        super(context,BASE_PUNTOS_MAPA ,null,DATABASE_VERSION);
        //iniciar el modelo
        mundo = new SistemaMio();

    }

    public void inicarDatos(Context context){
        //Iniciar las tablas de las bases de datos
        SQLiteDatabase db = this.getReadableDatabase();
        String q = "SELECT * FROM " + TABLA_WIFI;
        Cursor busqueda = db.rawQuery(q,null);
        if(!busqueda.moveToFirst()) {
            cargarDatosWifi();
        }
        q = "SELECT * FROM " + TABLA_PUNTOS_RECARGA;
        busqueda = db.rawQuery(q,null);
        if(!busqueda.moveToFirst()) {
            cargarDatosRecargas(context);
        }
        q = "SELECT * FROM " + TABLA_PARADAS;
        busqueda = db.rawQuery(q,null);
        if(!busqueda.moveToFirst()) {
            cargarDatosParadas(context);
        }


    }

    public SistemaMio getMundo() {
        return mundo;
    }

    public void setMundo(SistemaMio mundo) {
        this.mundo = mundo;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLA_WIFI);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLA_RECARGA);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLA_PARADA);
    }


    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void agregarWifi(String latitud, String longitud) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put(LATITUD_PUNTO_WIFI, latitud);
            valores.put(LONGITUD_PUNTO_WIFI, longitud);
            db.insertOrThrow(TABLA_WIFI, null, valores);
        }
    }
    public void agregarRecarga(String nom, String latitud, String longitud){
        SQLiteDatabase db = this.getWritableDatabase();
        if(db!=null){
            ContentValues valores = new ContentValues();
            valores.put(PUNTO_RECARGA, nom);
            valores.put(LATITUD_PUNTO_RECARGA, latitud);
            valores.put(LONGITUD_PUNTO_RECARGA, longitud );
            db.insertOrThrow(TABLA_PUNTOS_RECARGA, null, valores);
        }
    }
    public void agregarParada(String id, String nom, String latitud, String longitud){
        SQLiteDatabase db = this.getWritableDatabase();
        if(db!=null){
            ContentValues valores = new ContentValues();
            valores.put(ID_PARADA, id);
            valores.put(PARADA, nom);
            valores.put(LATITUD_PARADA, latitud);
            valores.put(LONGITUD_PARADA, longitud );
            db.insertOrThrow(TABLA_PARADAS, null, valores);
        }
    }


    public void cargarModeloPuntosWifi() {
        SQLiteDatabase db = this.getReadableDatabase();
        String q = "SELECT * FROM " + TABLA_WIFI;
        Cursor busqueda = db.rawQuery(q,null);

        if(busqueda.moveToFirst()){
            String latitud = busqueda.getString(0);
            String longitud = busqueda.getString(1);
            mundo.getEstacionesWifi().add(new PuntoMapa("", latitud, longitud));

            while (busqueda.moveToNext()){
                latitud = busqueda.getString(0);
                longitud = busqueda.getString(1);
                mundo.getEstacionesWifi().add(new PuntoMapa("", latitud, longitud));
            }
        }
    }
    public void cargarModeloPuntosRecarga() {
        SQLiteDatabase db = this.getReadableDatabase();
        String q = "SELECT * FROM " + TABLA_PUNTOS_RECARGA;
        Cursor busqueda = db.rawQuery(q,null);

        if(busqueda.moveToFirst()) {
            String nombre = busqueda.getString(0);
            String latitud = busqueda.getString(1);
            String longitud = busqueda.getString(2);
            mundo.getPuntosRecarga().add(new PuntoMapa(nombre, latitud, longitud));

            while (busqueda.moveToNext()){
             nombre = busqueda.getString(0);
             latitud = busqueda.getString(1);
             longitud = busqueda.getString(2);
             mundo.getPuntosRecarga().add(new PuntoMapa(nombre, latitud, longitud));
            }
        }
    }
    public void cargarModeloParadas() {
        SQLiteDatabase db = this.getReadableDatabase();
        String q = "SELECT * FROM " + TABLA_PARADAS;
        Cursor busqueda = db.rawQuery(q,null);

        if(busqueda.moveToFirst()) {
            String id = busqueda.getString(0);
            String nombre = busqueda.getString(1);
            String latitud = busqueda.getString(2);
            String longitud = busqueda.getString(3);
            mundo.getParadasDelSistema().add(new Parada(id, nombre, latitud, longitud));

            while (busqueda.moveToNext()){
                id = busqueda.getString(0);
                nombre = busqueda.getString(1);
                latitud = busqueda.getString(2);
                longitud = busqueda.getString(3);
                mundo.getParadasDelSistema().add(new Parada(id, nombre, latitud, longitud));
            }
        }

    }


    public void cargarDatosWifi() {
        String wifi = "3.367050,-76.529009" + "_3.372651,-76.539931" + "_3.377107,-76.542731" + "_3.388118, -76.544928" + "_3.392862, -76.545754" + "_3.398730, -76.546414" + "_3.403732, -76.546736" + "_3.409768, -76.547470" + "_3.414726, -76.548361" + "_3.415198, -76.549830" + "_3.418871, -76.548328" + "_3.423755, -76.546923" + "_3.431683, -76.543305" + "_3.434863, -76.541009" + "_3.439522, -76.537276" + "_3.442264, -76.532619" + "_3.442677, -76.527292" + "_3.443695, -76.526273" + "_3.427164, -76.505362" + "_3.418522, -76.486820" + "_3.444852, -76.508299" + "_3.444306, -76.502248" + "_3.443920, -76.498847"+ "_3.444846, -76.488224" + "_3.444050, -76.482942" + "_3.447367, -76.529831" + "_3.448727, -76.530174" + "_3.449466, -76.528050" + "_3.452368, -76.531258" + "_3.453161, -76.531644" + "_3.454532, -76.530120" + "_3.456759, -76.530281" + "_3.461235, -76.526966" + "_3.463591, -76.525217" + "_3.474333, -76.519649" + "_3.478142, -76.517279" + "_3.484364, -76.513438" + "_3.489322, -76.508556" + "_3.462833, -76.517471" + "_3.466195, -76.513265" + "_3.473739, -76.506703" + "_3.481682, -76.498078";
        String[] p0 = wifi.split("_");
        for (int i =0; i<p0.length;i++){
            String[] p1 = p0[i].split(",");
            agregarWifi(p1[0].toString(), p1[1].toString());
        }
    }
    public void cargarDatosParadas(Context context){
        try {
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.stops)));
            String linea = br.readLine();
            while (linea != null) {
                String lec[] = linea.split(",");
                agregarParada(lec[0],lec[1], lec[2], lec[3]);
                linea = br.readLine();
            }
        }catch (IOException e){

        }
    }
    public void cargarDatosRecargas(Context context){

        try{
            StringBuffer sb = new StringBuffer();
            BufferedReader br = null;
            try{
                br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.recargas)));
                String line;
                while((line = br.readLine()) != null){
                    sb.append(line);
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try{
                    br.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            JSONObject reader = new JSONObject(sb.toString());
            JSONArray entry  = (JSONArray) reader.getJSONObject("feed").getJSONArray("entry");
            for (int i = 0; i<entry.length();i++){
                String nom = entry.getJSONObject(i).getJSONObject("title").getString("$t");
                String coordenadas = entry.getJSONObject(i).getJSONObject("gsx$coordenadas").getString("$t");
                String latitud = coordenadas.split(",")[0];
                String longitud = coordenadas.split(",")[1];
                agregarRecarga(nom,latitud,longitud);
            }
        }catch (Exception e){

        }
    }

}
