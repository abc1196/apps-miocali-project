package com.example.apps_miocali_project.control;

/**
 * Created by Juan K on 13/11/2017.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.apps_miocali_project.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import Modelo.Destino;
import Modelo.Parada;
import Modelo.PuntoMapa;
import Modelo.Ruta;
import Modelo.SistemaMio;
import Modelo.Viaje;


public class DataBase extends SQLiteOpenHelper implements Serializable {

    private static String DB_PATH = "/data/data/com.example.apps_miocali_project/databases/";
    private static final String DATABASE_NAME = "basePuntosMapa.sqlite";
    private static final String LOCAL_DATABASE_NAME = "basePuntosMapa";



    private SistemaMio mundo;

    private static final int DATABASE_VERSION = 1;
    private static final String BASE_PUNTOS_MAPA = "basePuntosMapa";
    public static final String TABLA_WIFI = "EstacionesWifi";
    public static final String TABLA_PUNTOS_RECARGA  = "puntosRecarga";
    public static final String TABLA_PARADAS = "paradas";
    public static final String TABLA_RUTAS = "rutas";
    public static final String TABLA_STOPS = "RelacionRutasyParadas";


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

    private static final String ID_RUTA = "idRuta";
    private static final String TIPO_RUTA = "tipoRuta";
    private static final String NOM_RUTA = "nombreRuta";
    private static final String IDENT_RUTA = "identificadorRuta";
    private static final String ID_STOP = "idStop";






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

    public static final String SQL_CREATE_TABLA_RUTAS = "CREATE TABLE "
            + DataBase.TABLA_RUTAS + " ( "
            + ID_RUTA + " TEXT PRIMARY KEY, "
            + NOM_RUTA + " TEXT, "
            + TIPO_RUTA + " TEXT, "
            + IDENT_RUTA + " TEXT ) "
            ;

    public static final String SQL_CREATE_TABLA_STOPS = "CREATE TABLE "
            + DataBase.TABLA_STOPS + " ( "
            + ID_STOP + " TEXT PRIMARY KEY, "
            + ID_RUTA + " TEXT, "
            + ID_PARADA + " TEXT ) "
            ;

    public DataBase(Context context){
        super(context,BASE_PUNTOS_MAPA ,null,DATABASE_VERSION);
        //iniciar el modelo
        mundo = new SistemaMio();
        try {
            createDataBase(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createDataBase(Context context) throws IOException{
        //check if the database exists
        boolean databaseExist = checkDataBase();

       // if(databaseExist){
            // Do Nothing.
        //}else{
            SQLiteDatabase db = this.getReadableDatabase();
            copyDataBase(context);
        //}// end if else dbExist
    }

    public boolean checkDataBase(){
        /**
        boolean existe = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String q = "SELECT * FROM " + TABLA_WIFI;
        Cursor busqueda = db.rawQuery(q,null);
        if(!busqueda.moveToFirst()) {
            existe = true;
        }q = "SELECT * FROM " + TABLA_PUNTOS_RECARGA;
        busqueda = db.rawQuery(q,null);
        if(!busqueda.moveToFirst()) {
            existe = true;
        }
        q = "SELECT * FROM " + TABLA_PARADAS;
        busqueda = db.rawQuery(q,null);
        if(!busqueda.moveToFirst()) {
            existe = true;
        }
        q = "SELECT * FROM " + TABLA_RUTAS;
        busqueda = db.rawQuery(q,null);
        if(!busqueda.moveToFirst()) {
            existe = true;
        }
        q = "SELECT * FROM " + TABLA_STOPS;
        busqueda = db.rawQuery(q,null);
        if(!busqueda.moveToFirst()) {
            existe = true;
        }
        return  existe;
         */
        File databaseFile = new File(DB_PATH + DATABASE_NAME);
        return databaseFile.exists();
    }

    private void copyDataBase(Context context) throws IOException{
        SQLiteDatabase db = this.getWritableDatabase();
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        String outFileName = DB_PATH + LOCAL_DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
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
        q = "SELECT * FROM " + TABLA_RUTAS;
        busqueda = db.rawQuery(q,null);
        if(!busqueda.moveToFirst()) {
            cargarDatosRutas(context);
        }
        q = "SELECT * FROM " + TABLA_STOPS;
        busqueda = db.rawQuery(q,null);
        if(!busqueda.moveToFirst()) {
            cargarStops(context);
        }

    }

    public SistemaMio getMundo() {
        return mundo;
    }

    public void setMundo(SistemaMio mundo) {
        this.mundo = mundo;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /**
         sqLiteDatabase.execSQL(SQL_CREATE_TABLA_WIFI);
         sqLiteDatabase.execSQL(SQL_CREATE_TABLA_RECARGA);
         sqLiteDatabase.execSQL(SQL_CREATE_TABLA_PARADA);
         sqLiteDatabase.execSQL(SQL_CREATE_TABLA_RUTAS);
         sqLiteDatabase.execSQL(SQL_CREATE_TABLA_STOPS);
        */

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
    public void agregarRuta(String id, String nom, String tipo, String ident){
        SQLiteDatabase db = this.getWritableDatabase();
        if(db!=null){
            ContentValues valores = new ContentValues();
            valores.put(ID_RUTA, id);
            valores.put(NOM_RUTA, nom);
            valores.put(TIPO_RUTA, tipo);
            valores.put(IDENT_RUTA, ident );
            db.insertOrThrow(TABLA_RUTAS, null, valores);
        }
    }
    public void agregarStop(int i,String idRuta, String idParada){
        SQLiteDatabase db = this.getWritableDatabase();
        if(db!=null){
            ContentValues valores = new ContentValues();
            valores.put(ID_STOP, ""+i);
            valores.put(ID_RUTA, idRuta);
            valores.put(ID_PARADA, idParada);
            db.insertOrThrow(TABLA_STOPS, null, valores);
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
    public ArrayList<String> cargarRutasParada(String id_parada){

        ArrayList<String> rutas=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+ ID_RUTA+" FROM " + this.TABLA_STOPS +" WHERE "+this.ID_PARADA+" LIKE "+"'"+id_parada+"'";
        Cursor busqueda = db.rawQuery(query,null);
        if(busqueda.moveToFirst()){
            while (busqueda.moveToNext()){
                String id_ruta_busqueda = busqueda.getString(0);
                String query2 = "SELECT "+ this.IDENT_RUTA+" FROM " + this.TABLA_RUTAS +" WHERE "+this.ID_RUTA+" LIKE "+"'"+id_ruta_busqueda+"'";
                Cursor busqueda2 = db.rawQuery(query2,null);
                if(busqueda2.moveToFirst()){

                    String rutaActual=busqueda2.getString(0);
                    if(!rutas.contains(rutaActual)) {

                        rutas.add(rutaActual);
                    }
                }
            }
        }
        return rutas;
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
    public void cargarModeloRutas(){
        SQLiteDatabase db = this.getReadableDatabase();
        String q = "SELECT * FROM " + TABLA_RUTAS;
        Cursor busqueda = db.rawQuery(q,null);

        if(busqueda.moveToFirst()) {
            String id = busqueda.getString(0);
            String nombre = busqueda.getString(1);
            String tipo = busqueda.getString(2);
            String ident = busqueda.getString(3);
            mundo.getRutas().add(new Ruta(id, tipo, nombre, ident));

            while (busqueda.moveToNext()){
                id = busqueda.getString(0);
                nombre = busqueda.getString(1);
                tipo = busqueda.getString(2);
                ident = busqueda.getString(3);
                mundo.getRutas().add(new Ruta(id, tipo, nombre, ident));
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
                double latitud = Double.parseDouble(lec[3]);
                double longitud = Double.parseDouble(lec[4]);
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
    public void cargarDatosRutas(Context context){
        try {
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.routes)));
            String linea = br.readLine();
            while (linea != null) {
                String lec[] = linea.split(",");

                String tipo = "";
                if(lec[1].charAt(0) == 'A'){
                    tipo = "Alimentador";
                }else if(lec[1].charAt(0) == 'P'){
                    tipo = "Pre-Troncal";
                }else if(lec[1].charAt(0) == 'T'){
                    tipo = "Troncal";
                }else{
                    tipo = "Expreso";
                }

                agregarRuta(lec[0],lec[2], tipo, lec[1]);
                linea = br.readLine();
            }
        }catch (IOException e){

        }
    }
    public void cargarStops(Context context){
        try {
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.procesado)));
            String linea = br.readLine();
            int i = 1;
            while (linea != null) {
                String lec[] = linea.split(",");
                if(lec.length>=2) {
                    agregarStop(i, lec[0], lec[1]);
                    i++;
                }
                linea = br.readLine();
            }
        }catch (IOException e){
        }
    }

    public Viaje planearRuta(String x1, String y1, String x2, String y2){
      ConexionHTTPPRuta planeacionRuta = new ConexionHTTPPRuta(x1,y1,x2,y2);
      planeacionRuta.start();
      Viaje viaje = new Viaje();
      while (planeacionRuta.isAlive()){

      }
      try {
          String body = planeacionRuta.getRespuesta();
          JSONObject object = new JSONObject(body.toString());
          JSONObject route = object.getJSONObject("route");
          JSONArray sections = route.getJSONArray("sections");

          viaje.setLatitudOrigen(Double.parseDouble(x1));
          viaje.setLongitudOrigen(Double.parseDouble(y1));
          viaje.setLatitudDestino(Double.parseDouble(x2));
          viaje.setLongitudDestino(Double.parseDouble(y2));
          int i=0;
          int j=0;
          for(i = 0; i < sections.length();i++){
              JSONArray locations = sections.getJSONObject(i).getJSONArray("locations");
              String type = sections.getJSONObject(i).getString("type");
              String bus = "";
              if(type.equals("JOURNEY")){
                  bus = sections.getJSONObject(i).getString("name");
              }else{
                  bus = "Caminata";
              }
              for (j = 0;j < locations.length();j++){
                  JSONObject loc = locations.getJSONObject(j);
                  String arr = loc.getString("arr");
                  String dep = loc.getString("dep");
                  String nom = loc.getString("name");
                  double latitud = Double.parseDouble(locations.getJSONObject(j).getString("y").substring(0,1)+"."+locations.getJSONObject(j).getString("y").substring(1,locations.getJSONObject(j).getString("y").length()));
                  double longitud = Double.parseDouble(locations.getJSONObject(j).getString("x").substring(0,3)+"."+locations.getJSONObject(j).getString("x").substring(3,locations.getJSONObject(j).getString("y").length()));
                  Destino dest = new Destino(nom, arr, dep, longitud, latitud, bus);
                  viaje.getDestinos().add(dest);
              }

          }
          viaje.setHoraSalida(sections.getJSONObject(0).getJSONArray("locations").getJSONObject(0).getString("dep"));
          viaje.setHoraLlegada(sections.getJSONObject(i-1).getJSONArray("locations").getJSONObject(j-1).getString("arr"));
      }catch(Exception e){

      }
        mundo.setViaje(viaje);
        return  viaje;
    }

}
