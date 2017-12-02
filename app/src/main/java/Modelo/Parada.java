package Modelo;

import java.io.Serializable;

/**
 * Created by Juan K on 13/11/2017.
 */

public class Parada implements Serializable {

    private String id;
    private String nombre;
    private Double longitud;
    private Double latitud;


    public Parada(String id, String nom, String latit, String longit){
        double lati = Double.parseDouble(latit);
        double longi = Double.parseDouble(longit);
        nombre = nom;
        latitud = lati;
        longitud = longi;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getLongitud() {
        return longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

}
