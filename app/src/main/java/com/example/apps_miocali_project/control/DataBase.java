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
import java.util.ArrayList;
import java.util.Iterator;


import org.json.*;


public class DataBase extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String BASE_PUNTOS_MAPA = "basePuntosMapa";
    private static final String TABLA_WIFI = "EstacionesWifi";
    private static final String TABLA_PUNTOS_RECARGA  = "puntosRecarga";
    private static final String TABLA_PARADAS = "paradas";


    private static final String PUNTO_RECARGA = "nombrePuntoRecarga";
    private static final String LATITUD_PUNTO_RECARGA = "latitudRecarga";
    private static final String LONGITUD_PUNTO_RECARGA = "longitudRecarga";

    private static final String PUNTO_WIFI = "nombrePuntoWifi";
    private static final String LATITUD_PUNTO_WIFI = "latitudWifi";
    private static final String LONGITUD_PUNTO_WIFI = "longitudWifi";

    private static final String PARADA = "nombreParada";
    private static final String LATITUD_PARADA = "latitudParada";
    private static final String LONGITUD_PARADA = "longitudParada";




    public static final String SQL_CREATE_TABLA_WIFI = "CREATE TABLE "
            + DataBase.TABLA_WIFI + " ( "
            + PUNTO_WIFI + " PRIMARY KEY, "
            + LATITUD_PUNTO_WIFI + " TEXT, "
            + LONGITUD_PUNTO_WIFI + " TEXT, "
            ;

    public static final String SQL_CREATE_TABLA_RECARGA = "CREATE TABLE "
            + DataBase.TABLA_PUNTOS_RECARGA + " ( "
            + PUNTO_RECARGA + " PRIMARY KEY, "
            + LATITUD_PUNTO_RECARGA + " TEXT, "
            + LONGITUD_PUNTO_RECARGA + " TEXT, "
            ;

    public static final String SQL_CREATE_TABLA_PARADA = "CREATE TABLE "
            + DataBase.TABLA_PARADAS + " ( "
            + PARADA + " PRIMARY KEY, "
            + LATITUD_PARADA + " TEXT, "
            + LONGITUD_PARADA + " TEXT, "
            ;

    public DataBase(Context context){
        super(context,BASE_PUNTOS_MAPA ,null,DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLA_WIFI);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLA_RECARGA);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLA_PARADA);
    }


    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void agregarWifi(String nom, String latitud, String longitud) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put(PUNTO_WIFI, nom);
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

    public void agregarParada(String nom, String latitud, String longitud){
        SQLiteDatabase db = this.getWritableDatabase();
        if(db!=null){
            ContentValues valores = new ContentValues();
            valores.put(PARADA, nom);
            valores.put(LATITUD_PARADA, latitud);
            valores.put(LONGITUD_PARADA, longitud );
            db.insertOrThrow(TABLA_PARADAS, null, valores);
        }
    }

    public ArrayList<String> buscarPuntosWifi() {
        ArrayList<String> puntosWifi = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String q = "SELECT * FROM " + TABLA_WIFI;
        Cursor busqueda = db.rawQuery(q,null);

        if(busqueda.moveToFirst()) {
            String actual = busqueda.getString(0);
            for (int i = 1; i < 2; i++) {
                actual = actual + "_" + busqueda.getString(i);
            }
            puntosWifi.add(actual);
            while (busqueda.moveToNext()){
                actual = busqueda.getString(0);
                for (int i = 1; i < 2; i++) {
                    actual = actual + "_" + busqueda.getString(i);
                }
                puntosWifi.add(actual);
            }
        }
        return puntosWifi;
    }

    public ArrayList<String> buscarPuntosRecarga() {
        ArrayList<String> puntosRecarga = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String q = "SELECT * FROM " + TABLA_PUNTOS_RECARGA;
        Cursor busqueda = db.rawQuery(q,null);

        if(busqueda.moveToFirst()) {
            String actual = busqueda.getString(0);
            for (int i = 1; i < 2; i++) {
                actual = actual + "_" + busqueda.getString(i);
            }
            puntosRecarga.add(actual);
            while (busqueda.moveToNext()){
                actual = busqueda.getString(0);
                for (int i = 1; i < 2; i++) {
                    actual = actual + "_" + busqueda.getString(i);
                }
                puntosRecarga.add(actual);
            }
        }
        return puntosRecarga;
    }

    public ArrayList<String> buscarParadas() {
        ArrayList<String> puntosParadas = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String q = "SELECT * FROM " + TABLA_PARADAS;
        Cursor busqueda = db.rawQuery(q,null);

        if(busqueda.moveToFirst()) {
            String actual = busqueda.getString(0);
            for (int i = 1; i < 2; i++) {
                actual = actual + "_" + busqueda.getString(i);
            }
            puntosParadas.add(actual);
            while (busqueda.moveToNext()){
                actual = busqueda.getString(0);
                for (int i = 1; i < 2; i++) {
                    actual = actual + "_" + busqueda.getString(i);
                }
                puntosParadas.add(actual);
            }
        }
        return puntosParadas;
    }

    public void cargarDatosWifi() {
        String wifi = "3.367050,-76.529009" + "_3.372651,-76.539931" + "_3.377107,-76.542731" + "_3.388118, -76.544928" + "_3.392862, -76.545754" + "_3.398730, -76.546414" + "_3.403732, -76.546736" + "_3.409768, -76.547470" + "_3.414726, -76.548361" + "_3.415198, -76.549830" + "_3.418871, -76.548328" + "_3.423755, -76.546923" + "_3.431683, -76.543305" + "_3.434863, -76.541009" + "_3.439522, -76.537276" + "_3.442264, -76.532619" + "_3.442677, -76.527292" + "_3.443695, -76.526273" + "_3.427164, -76.505362" + "_3.418522, -76.486820" + "_3.444852, -76.508299" + "_3.444306, -76.502248" + "_3.443920, -76.498847"+ "_3.444846, -76.488224" + "_3.444050, -76.482942" + "_3.447367, -76.529831" + "_3.448727, -76.530174" + "_3.449466, -76.528050" + "_3.452368, -76.531258" + "_3.453161, -76.531644" + "_3.454532, -76.530120" + "_3.456759, -76.530281" + "_3.461235, -76.526966" + "_3.463591, -76.525217" + "_3.474333, -76.519649" + "_3.478142, -76.517279" + "_3.484364, -76.513438" + "_3.489322, -76.508556" + "_3.462833, -76.517471" + "_3.466195, -76.513265" + "_3.473739, -76.506703" + "_3.481682, -76.498078";
        String[] p0 = wifi.split("_");
        for (int i =0; i<p0.length;i++){
            String[] p1 = p0[i].split(",");
            agregarWifi("", p1[1].toString(), p1[2].toString());
        }
    }

    public void cargarDatosParadas(){
        File archivo = new File("./data/stops.txt");
        try {
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();
            while (linea != null) {
                String lec[] = linea.split(",");
                agregarParada(lec[1], lec[2], lec[3]);
                linea = br.readLine();
            }
        }catch (IOException e){

        }
    }
    public void cargarDatosRecargas(){
        try{
            JSONObject reader = new JSONObject("./data/recargas.JSON");
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
