package com.example.apps_miocali_project.control;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;

import Modelo.Bus;


/**
 * Created by Jorge Castaño on 19/11/2017.
 */

public class ConexionHTTPTReal extends Thread{

    private String respuesta;
    private boolean mantener;
    private boolean consultaLista;
    private ArrayList<Bus> buses;
private HttpURLConnection urlConnection;

    public ConexionHTTPTReal() {
        consultaLista = false;
        mantener = true;
        buses = new ArrayList<Bus>();
    }

    public ArrayList<Bus> getBuses() {
        return buses;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void run() {
        super.run();
        try {
            while(mantener == true) {
                consultaLista = false;
                buses = clienteHttp("http://190.216.202.35:90/gtfs/realtime/");
                sleep(10000);
            }
        }catch (IOException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setMantener(boolean mantener) {
        this.mantener = mantener;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public void setConsultaLista(boolean consultaLista) {
        this.consultaLista = consultaLista;
    }

    public void setBuses(ArrayList<Bus> buses) {
        this.buses = buses;
    }

    public void setUrlConnection(HttpURLConnection urlConnection) {
        this.urlConnection = urlConnection;
    }

    public ArrayList<Bus> clienteHttp(String dirweb) throws IOException {

        ArrayList<Bus>  buses = new ArrayList<Bus>();

        try {

            URL url = new URL(dirweb);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            Integer codigoRespuesta = urlConnection.getResponseCode();

            if(codigoRespuesta==HttpURLConnection.HTTP_UNAUTHORIZED){
                Authenticator.setDefault(new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("appconcurso", "JcYbIry5sA".toCharArray());
                    }
                });
            }

            url = new URL("http://190.216.202.35:90/gtfs/realtime/vehiclePositions.pb");
            FeedMessage feed = FeedMessage.parseFrom(url.openStream());
            for (FeedEntity entity : feed.getEntityList()) {
                if (entity.hasVehicle()) {
                    double latitud = (double) entity.getVehicle().getPosition().getLatitude();
                    double longitud = (double) entity.getVehicle().getPosition().getLongitude();
                    String routeId = entity.getVehicle().getTrip().getRouteId();
                    String stopId = entity.getVehicle().getStopId().toString();

                    Bus n = new Bus(latitud, longitud, routeId, stopId);
                    buses.add(n);
                }
            }
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            e.toString(); //Error URL incorrecta
            e.printStackTrace();
        } catch (SocketTimeoutException e){
            e.toString(); //Error: Finalizado el timeout esperando la respuesta del servidor.
            e.printStackTrace();
        } catch (Exception e) {
            e.toString();//Error diferente a los anteriores.
            e.printStackTrace();
        }
        consultaLista = true;
        return buses;
    }

    public boolean isMantener() {
        return mantener;
    }

    public boolean isConsultaLista() {
        return consultaLista;
    }

    public HttpURLConnection getUrlConnection() {
        return urlConnection;
    }

    private String readStream(InputStream in) throws IOException{

        BufferedReader r = null;
        r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        if(r != null){
            r.close();
        }
        in.close();
        return total.toString();
    }

}
