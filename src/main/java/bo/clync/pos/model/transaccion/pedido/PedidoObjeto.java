package bo.clync.pos.model.transaccion.pedido;

import java.util.Date;
import java.util.List;

/**
 * Created by eyave on 27-10-17.
 */
public class PedidoObjeto {

    private String id;
    private Date fechaMovimiento;
    private Integer nroMovimiento;
    private String codigoProveedor;
    private String observacion;
    private List<PedidoDetalle> lista;

    public PedidoObjeto(){}

    public PedidoObjeto(String id, Date fechaMovimiento, Integer nroMovimiento, String codigoProveedor, String observacion){
        this.id = id;
        this.fechaMovimiento = fechaMovimiento;
        this.nroMovimiento = nroMovimiento;
        this.codigoProveedor = codigoProveedor;
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

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getCodigoProveedor() {
        return this.codigoProveedor;
    }

    public void setCodigoProveedor(String codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<PedidoDetalle> getLista() {
        return lista;
    }

    public void setLista(List<PedidoDetalle> lista) {
        this.lista = lista;
    }

}
