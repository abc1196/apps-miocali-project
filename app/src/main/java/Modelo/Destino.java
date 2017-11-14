package Modelo;

/**
 * Created by Juan K on 13/11/2017.
 */

public class Destino {

    private long longitudDestino;
    private long latitudDestino;
    private String nombreDestino;

    public Destino(){

    }

    public long getLongitudDestino() {
        return longitudDestino;
    }

    public long getLatitudDestino() {
        return latitudDestino;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public void setLongitudDestino(long longitudDestino) {
        this.longitudDestino = longitudDestino;
    }

    public void setLatitudDestino(long latitudDestino) {
        this.latitudDestino = latitudDestino;
    }

    public void setNombreDestino(String nombreDestino) {
        this.nombreDestino = nombreDestino;
    }
}
