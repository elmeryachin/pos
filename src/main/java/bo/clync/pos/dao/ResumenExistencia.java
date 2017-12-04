package bo.clync.pos.dao;

/**
 * Created by eyave on 03-12-17.
 */
public class ResumenExistencia {

    private String codigoAmbiente;
    private Integer cantidad;

    public ResumenExistencia(){
    }

    public ResumenExistencia(String codigoAmbiente, Integer cantidad){
        this.codigoAmbiente = codigoAmbiente;
        this.cantidad = cantidad;
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
}
