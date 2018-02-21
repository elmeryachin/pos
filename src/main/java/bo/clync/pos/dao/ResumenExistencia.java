package bo.clync.pos.dao;

/**
 * Created by eyave on 03-12-17.
 */
public class ResumenExistencia {

    private String nombreAmbiente;
    private String codigoAmbiente;
    private Integer cantidad;
    private int propio;

    public ResumenExistencia(){
    }

    public ResumenExistencia(String nombreAmbiente, String codigoAmbiente, Integer cantidad, int propio){
        this.setNombreAmbiente(nombreAmbiente);
        this.setCodigoAmbiente(codigoAmbiente);
        this.setCantidad(cantidad);
        this.setPropio(propio);
    }

    public String getNombreAmbiente() {
        return nombreAmbiente;
    }

    public void setNombreAmbiente(String nombreAmbiente) {
        this.nombreAmbiente = nombreAmbiente;
    }

    public String getCodigoAmbiente() {
        return codigoAmbiente;
    }

    public void setCodigoAmbiente(String codigoAmbiente) {
        this.codigoAmbiente = codigoAmbiente;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        if(cantidad==null) this.cantidad = 0;
        else this.cantidad = cantidad;
    }

    public int getPropio() {
        return propio;
    }

    public void setPropio(int propio) {
        this.propio = propio;
    }
}
