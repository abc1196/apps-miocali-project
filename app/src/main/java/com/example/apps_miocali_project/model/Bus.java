package com.example.apps_miocali_project.model;

import java.io.Serializable;

/**
 * Created by Juan K on 13/11/2017.
 */

public class Bus implements Serializable {

    private String idEntidad;
    private String tripId;
    private String routeId;
    private Double latitud;
    private Double longitud;
    private String idBus;
    private String placa;
    private String identRuta;
    private String stopId;


    public Bus(double latitud, double longitud, String routeId, String stopId){
        this.latitud = latitud;
        this.longitud = longitud;
        this.routeId = routeId;
        this.stopId = stopId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getIdentRuta() {
        return identRuta;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public void setIdentRuta(String identRuta) {
        this.identRuta = identRuta;
    }

    public String getIdEntidad() {
        return idEntidad;
    }

    public String getTripId() {
        return tripId;
    }

    public String getRouteId() {
        return routeId;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getIdBus() {
        return idBus;
    }

    public String getPlaca() {
        return placa;
    }

    public void setIdEntidad(String idEntidad) {
        this.idEntidad = idEntidad;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public void setLatitud(double  latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public void setIdBus(String idBus) {
        this.idBus = idBus;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
}
