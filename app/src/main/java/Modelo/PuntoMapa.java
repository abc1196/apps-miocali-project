package Modelo;

/**
 * Created by Juan K on 15/11/2017.
 */

public class PuntoMapa {
    private String nombre;
    private long latitud;
    private long longitud;

    public PuntoMapa(String nom, long lati, long longi){
        nombre = nom;
        latitud = lati;
        longitud = longi;
    }

    public PuntoMapa(String nom, String latit, String longit){

        long lati = Long.parseLong(latit);
        long longi = Long.parseLong(longit);
        nombre = nom;
        latitud = lati;
        longitud = longi;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLatitud(long latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(long longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {

        return nombre;
    }

    public long getLatitud() {
        return latitud;
    }

    public long getLongitud() {
        return longitud;
    }
}
