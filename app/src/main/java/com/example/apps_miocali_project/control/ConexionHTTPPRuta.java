package com.example.apps_miocali_project.control;

import android.util.Log;

import java.io.BufferedReader;
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
 * Created by Juan Swan on 26/11/2017.
 */

public class ConexionHTTPPRuta extends Thread{

    private String respuesta;
    private String x1;
    private String y1;
    private String x2;
    private String y2;
    String ruta;
private HttpURLConnection urlConnection;

    public ConexionHTTPPRuta(String x1, String y1, String x2, String y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y2 = y2;
        this.y1 = y1;
        ruta ="http://tuyo.herokuapp.com/request-route?x1=" + this.x1 + "&y1=" + this.y1+ "&x2=" + this.x2 + "&y2=" + this.y2 + "&mode=lessBuses";
        Log.d("ruta", ruta);
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void run() {
        super.run();

        try {
            respuesta = clienteHttp(ruta);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String clienteHttp(String dirweb) throws IOException {

        String body = " ";

        try {

            URL url = new URL(dirweb);
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
