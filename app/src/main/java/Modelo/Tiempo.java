package Modelo;

/**
 * Created by alejo on 30/11/2017.
 */

public class Tiempo {

    private String bus;

    private String destino;

    private int tiempo;

    private String min;

    private boolean proximo;

    public Tiempo() {
        super();
        this.proximo=false;
    }

    public Tiempo(String bus, String destino, int tiempo, String min) {
        this.bus = bus;
        this.destino = destino;
        this.tiempo = tiempo;
        this.min = min;
        this.proximo=false;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public boolean isProximo() {
        return proximo;
    }

    public void setProximo(boolean proximo) {
        this.proximo = proximo;
    }
}
