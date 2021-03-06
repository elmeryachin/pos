package bo.clync.pos.arquetipo.objetos.transaccion.generic;

import java.math.BigDecimal;

public class TransaccionDetalle {
    private String id;
    private String codigoArticulo;
    private Integer cantidad;
    private BigDecimal precio;
    private String observacion;

    public TransaccionDetalle(){}

    public TransaccionDetalle(String id, String codigoArticulo, Integer cantidad, BigDecimal precio, String observacion){
        this.id = id;
        this.codigoArticulo = codigoArticulo;
        this.cantidad = cantidad;
        this.precio = precio;
        this.observacion = observacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigoArticulo() {
        return codigoArticulo;
    }

    public void setCodigoArticulo(String codigoArticulo) {
        this.codigoArticulo = codigoArticulo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
