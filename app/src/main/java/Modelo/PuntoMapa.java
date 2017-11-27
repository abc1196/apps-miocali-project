package Modelo;

/**
 * Created by Juan K on 15/11/2017.
 */

public class PuntoMapa {
    private String nombre;
    private Double latitud;
    private Double longitud;

    public PuntoMapa(String nom, Double lati, Double longi){
        nombre = nom;
        latitud = lati;
        longitud = longi;
    }

    public PuntoMapa(String nom, String latit, String longit){

        double lati = Double.parseDouble(latit);
        double longi = Double.parseDouble(longit);
        nombre = nom;
        latitud = lati;
        longitud = longi;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {

        return nombre;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }
}
