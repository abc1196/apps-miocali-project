package Modelo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Juan K on 13/11/2017.
 */

public class SistemaMio implements Serializable {

    private ArrayList<Ruta> rutas;

    private ArrayList<PuntoMapa> estacionesWifi;
    private ArrayList<PuntoMapa> puntosRecarga;
    private ArrayList<Parada> paradasDelSistema;

    private Viaje viaje;



    public SistemaMio(){
        estacionesWifi = new ArrayList<PuntoMapa>();
        puntosRecarga = new ArrayList<PuntoMapa>();
        rutas = new ArrayList<Ruta>();
        paradasDelSistema = new ArrayList<Parada>();
    }

    public ArrayList<Parada> getParadasDelSistema() {
        return paradasDelSistema;
    }

    public void setParadasDelSistema(ArrayList<Parada> paradasDelSistema) {
        this.paradasDelSistema = paradasDelSistema;
    }

    public Parada getParada(String idparada){
        Parada parada=null;
        boolean termino=false;
        for(int i=0; i < paradasDelSistema.size()&&!termino;i++){
            Parada p=paradasDelSistema.get(i);
            if(p.getId().equals(idparada)){
                parada=p;
                termino=true;
            }
        }
        return parada;
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
