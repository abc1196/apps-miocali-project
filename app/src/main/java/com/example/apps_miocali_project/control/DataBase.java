package com.example.apps_miocali_project.control;

/**
 * Created by Juan K on 13/11/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

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
}
