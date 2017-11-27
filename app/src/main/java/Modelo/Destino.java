package Modelo;

/**
 * Created by Juan K on 13/11/2017.
 */

public class Destino {

    private double longitudDestino;
    private double latitudDestino;
    private String nombreDestino;

    public Destino(){

    }

    public double getLongitudDestino() {
        return longitudDestino;
    }

    public double getLatitudDestino() {
        return latitudDestino;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public void setLongitudDestino(double longitudDestino) {
        this.longitudDestino = longitudDestino;
    }

    public void setLatitudDestino(double latitudDestino) {
        this.latitudDestino = latitudDestino;
    }

    public void setNombreDestino(String nombreDestino) {
        this.nombreDestino = nombreDestino;
    }
}
