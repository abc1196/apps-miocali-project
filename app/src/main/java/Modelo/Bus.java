package Modelo;

/**
 * Created by Juan K on 13/11/2017.
 */

public class Bus {

    private String idEntidad;
    private String tripId;
    private String routeId;
    private Double latitud;
    private Double longitud;
    private String idBus;
    private String placa;

    public Bus(){

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
