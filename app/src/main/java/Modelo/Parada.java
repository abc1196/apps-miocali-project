package Modelo;

/**
 * Created by Juan K on 13/11/2017.
 */

public class Parada {

    private String id;
    private String nombre;
    private long longitud;
    private long latitud;
    private boolean estacionConWifi;

    public Parada(){

    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public long getLongitud() {
        return longitud;
    }

    public long getLatitud() {
        return latitud;
    }

    public boolean isEstacionConWifi() {
        return estacionConWifi;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLongitud(long longitud) {
        this.longitud = longitud;
    }

    public void setLatitud(long latitud) {
        this.latitud = latitud;
    }

    public void setEstacionConWifi(boolean estacionConWifi) {
        this.estacionConWifi = estacionConWifi;
    }
}
