package bo.clync.pos.arquetipo.objetos;

import java.util.Date;

public class ProcesoResumen {

    private String operador;
    private String codigoAmbiente;
    private String proceso;
    private Date fecha;


    public ProcesoResumen(String operador, String codigoAmbiente, String proceso, Date fecha) {
        this.operador = operador;
        this.codigoAmbiente = codigoAmbiente;
        this.proceso = proceso;
        this.fecha = fecha;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getCodigoAmbiente() {
        return codigoAmbiente;
    }

    public void setCodigoAmbiente(String codigoAmbiente) {
        this.codigoAmbiente = codigoAmbiente;
    }
}
