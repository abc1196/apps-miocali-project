package Modelo;

import java.util.ArrayList;

/**
 * Created by Juan K on 13/11/2017.
 */

public class Viaje {

    private double longitudOrigen;
    private double latitudOrigen;

    private double longitudDestino;
    private double latitudDestino;

    private String horaSalida;
    private String horaLlegada;



    private Ruta rutaOptima;

    private ArrayList<Destino> destinos;
    private ArrayList<Bus> buses;

    public Viaje(){
        destinos = new ArrayList<Destino>();
        latitudDestino=0;
        longitudDestino=0;
        latitudOrigen=0;
        longitudOrigen=0;
        horaSalida = "";
        horaLlegada = "";

    }

    public double getLogitudOrigen() {
        return longitudOrigen;
    }

    public double getLatitudOrigen() {
        return latitudOrigen;
    }

    public Ruta getRutaOptima() {
        return rutaOptima;
    }

    public void setLongitudDestino(double logitudDestino) {
        this.longitudDestino = logitudDestino;
    }

    public void setLatitudDestino(double latitudDestino) {
        this.latitudDestino = latitudDestino;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public void setHoraLlegada(String horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public double getLongitudDestino() {

        return longitudDestino;
    }

    public double getLatitudDestino() {
        return latitudDestino;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public String getHoraLlegada() {
        return horaLlegada;
    }

    public ArrayList<Destino> getDestinos() {
        return destinos;
    }

    public double getLongitudOrigen() {
        return longitudOrigen;
    }

    public ArrayList<Bus> getBuses() {
        return buses;
    }

    public void setLongitudOrigen(double logitudOrigen) {
        this.longitudOrigen = logitudOrigen;
    }

    public void setLatitudOrigen(double latitudOrigen) {
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
