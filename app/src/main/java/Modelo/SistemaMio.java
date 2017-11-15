package Modelo;

import java.util.ArrayList;

/**
 * Created by Juan K on 13/11/2017.
 */

public class SistemaMio {

    private ArrayList<Ruta> rutas;

    private ArrayList<PuntoMapa> estacionesWifi;
    private ArrayList<PuntoMapa> puntosRecarga;

    private Viaje viaje;



    public SistemaMio(){
        estacionesWifi = new ArrayList<PuntoMapa>();
        puntosRecarga = new ArrayList<PuntoMapa>();
        rutas = new ArrayList<Ruta>();

    }

    public void setRutas(ArrayList<Ruta> rutas) {
        this.rutas = rutas;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }

    public ArrayList<Ruta> getRutas() {

        return rutas;
    }
    public ArrayList<PuntoMapa> getEstacionesWifi() {
        return estacionesWifi;
    }

    public ArrayList<PuntoMapa> getPuntosRecarga() {
        return puntosRecarga;
    }

    public void setEstacionesWifi(ArrayList<PuntoMapa> estacionesWifi) {
        this.estacionesWifi = estacionesWifi;
    }

    public void setPuntosRecarga(ArrayList<PuntoMapa> puntosRecarga) {
        this.puntosRecarga = puntosRecarga;
    }

    public Viaje getViaje() {
        return viaje;
    }
}
