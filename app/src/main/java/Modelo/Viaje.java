package Modelo;

import java.util.ArrayList;

/**
 * Created by Juan K on 13/11/2017.
 */

public class Viaje {

    private long logitudOrigen;
    private long latitudOrigen;

    private Ruta rutaOptima;
    private ArrayList<Destino> destinos;
    private ArrayList<Bus> buses;

    public Viaje(){

    }

    public long getLogitudOrigen() {
        return logitudOrigen;
    }

    public long getLatitudOrigen() {
        return latitudOrigen;
    }

    public Ruta getRutaOptima() {
        return rutaOptima;
    }

    public ArrayList<Destino> getDestinos() {
        return destinos;
    }

    public ArrayList<Bus> getBuses() {
        return buses;
    }

    public void setLogitudOrigen(long logitudOrigen) {
        this.logitudOrigen = logitudOrigen;
    }

    public void setLatitudOrigen(long latitudOrigen) {
        this.latitudOrigen = latitudOrigen;
    }

    public void setRutaOptima(Ruta rutaOptima) {
        this.rutaOptima = rutaOptima;
    }

    public void setDestinos(ArrayList<Destino> destinos) {
        this.destinos = destinos;
    }

    public void setBuses(ArrayList<Bus> buses) {
        this.buses = buses;
    }
}
