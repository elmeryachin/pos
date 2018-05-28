package bo.clync.pos.dao.transaccion.generic;

import bo.clync.pos.utilitarios.UtilsGeneral;

import java.util.Date;
import java.util.List;

public class TransaccionObjeto {

    private String id;
    private String fechaMovimiento;
    private Integer nroMovimiento;
    private String codigo;
    private String observacion;
    private List<TransaccionDetalle> lista;

    public TransaccionObjeto(){}

    public TransaccionObjeto(String id, Date fechaMovimiento, Integer nroMovimiento, String codigo, String observacion){
        this.id = id;
        this.fechaMovimiento = UtilsGeneral.fecha(fechaMovimiento);
        this.nroMovimiento = nroMovimiento;
        this.codigo = codigo;
        this.observacion = observacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNroMovimiento() {
        return nroMovimiento;
    }

    public void setNroMovimiento(Integer nroMovimiento) {
        this.nroMovimiento = nroMovimiento;
    }

    public String getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(String fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<TransaccionDetalle> getLista() {
        return lista;
    }

    public void setLista(List<TransaccionDetalle> lista) {
        this.lista = lista;
    }

}
