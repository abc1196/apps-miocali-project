package Modelo;

/**
 * Created by Juan K on 13/11/2017.
 */

public class Destino {

    private double longitudDestino;
    private double latitudDestino;
    private String nombreDestino;
    private String tiempoLlegada;
    private String tiempoSalida;



    public Destino(String nombreDestino, String tiempoLlegada, String tiempoSalida, double longitudDestino, double latitudDestino){
            this.nombreDestino = nombreDestino;
            this.tiempoLlegada = tiempoLlegada;
            this. tiempoSalida = tiempoSalida;
            this.longitudDestino = longitudDestino;
            this.latitudDestino = latitudDestino;
    }

    public void setTiempoLlegada(String tiempoLlegada) {
        this.tiempoLlegada = tiempoLlegada;
    }

    public void setTiempoSalida(String tiempoSalida) {
        this.tiempoSalida = tiempoSalida;
    }

    public String getTiempoLlegada() {
        return tiempoLlegada;
    }

    public String getTiempoSalida() {
        return tiempoSalida;
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
