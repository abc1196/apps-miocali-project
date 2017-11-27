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

/**
 * Created by Jorge Castaño on 19/11/2017.
 */

public class ConexionHTTPTReal extends Thread{

    private String respuesta;
    private boolean mantener;
private HttpURLConnection urlConnection;

    public ConexionHTTPTReal() {
        start();
        mantener = true;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void run() {
        super.run();

        try {
            while (true) {
                respuesta = clienteHttp("http://190.216.202.35:90/gtfs/realtime/");
                sleep(30000);
            }


        }catch (IOException e) {
          e.printStackTrace();
        }
        catch (InterruptedException e){
            e.getStackTrace();
        }
    }

    public String clienteHttp(String dirweb) throws IOException {

        String body = " ";

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

            url = new URL(dirweb+"vehiclePositions.pb.txt");
            urlConnection = (HttpURLConnection) url.openConnection();
            body = readStream(urlConnection.getInputStream());


            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            body = e.toString(); //Error URL incorrecta
            e.printStackTrace();
        } catch (SocketTimeoutException e){
            body = e.toString(); //Error: Finalizado el timeout esperando la respuesta del servidor.
            e.printStackTrace();
        } catch (Exception e) {
            body = e.toString();//Error diferente a los anteriores.
            e.printStackTrace();
        }
        return body;
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
