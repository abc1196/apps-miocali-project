package Modelo;

import java.util.ArrayList;

/**
 * Created by Juan K on 13/11/2017.
 */

public class SistemaMio {

    private ArrayList<Ruta> rutas;
    private Viaje viaje;

    private SistemaMio(){

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

    public Viaje getViaje() {
        return viaje;
    }
}
