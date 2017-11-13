package Modelo;

/**
 * Created by Juan K on 13/11/2017.
 */

public class Bus {

    private String idEntidad;
    private String tripId;
    private String routeId;
    private long latitud;
    private long longitud;
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

    public long getLatitud() {
        return latitud;
    }

    public long getLongitud() {
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

    public void setLatitud(long latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(long longitud) {
        this.longitud = longitud;
    }

    public void setIdBus(String idBus) {
        this.idBus = idBus;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
}
