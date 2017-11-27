package Modelo;

import java.util.ArrayList;

/**
 * Created by Juan K on 13/11/2017.
 */

public class Ruta {


    private String idRuta;
    private String  tipo;
    private String nombre;
    private String identificador;

    private ArrayList<Parada> paradas;



    public Ruta(String id, String tipo, String nom, String ident){
        idRuta = id;
        this.tipo = tipo;
        nombre = nom;
        identificador= ident;

    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getIdRuta() {
        return idRuta;
    }

    public String getTipo() {
        return tipo;
    }

    public ArrayList<Parada> getParadas() {
        return paradas;
    }

    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setParadas(ArrayList<Parada> paradas) {
        this.paradas = paradas;
    }
}
