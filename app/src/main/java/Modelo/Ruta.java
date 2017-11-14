package Modelo;

import java.util.ArrayList;

/**
 * Created by Juan K on 13/11/2017.
 */

public class Ruta {
    public static final int TRONCAL = 1;
    public static final int PRE_TRONCAL = 2;
    public static final int EXPRESO  = 3;
    public static final int ALIMENTADOR = 4;

    private String idRuta;
    private int tipo;

    private ArrayList<Parada> paradas;

    public Ruta(){

    }

    public String getIdRuta() {
        return idRuta;
    }

    public int getTipo() {
        return tipo;
    }

    public ArrayList<Parada> getParadas() {
        return paradas;
    }

    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setParadas(ArrayList<Parada> paradas) {
        this.paradas = paradas;
    }
}
