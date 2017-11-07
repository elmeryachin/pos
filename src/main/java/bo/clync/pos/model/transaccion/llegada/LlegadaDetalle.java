package bo.clync.pos.model.transaccion.llegada;

import java.math.BigDecimal;

/**
 * Created by eyave on 06-11-17.
 */
public class LlegadaDetalle {
    private String id;
    private String codigoArticulo;
    private Integer cantidad;
    private BigDecimal precio;
    private Integer cantidadOficial;
    private BigDecimal precioOficial;
    private String observacion;

    public LlegadaDetalle(){}

    public LlegadaDetalle(String id, String codigoArticulo, Integer cantidad, BigDecimal precio, Integer cantidadOficial, BigDecimal precioOficial, String observacion){
        this.id = id;
        this.codigoArticulo = codigoArticulo;
        this.cantidad = cantidad;
        this.precio = precio;
        this.cantidadOficial = cantidadOficial;
        this.precioOficial = precioOficial;
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

    public Integer getCantidadOficial() {
        return cantidadOficial;
    }

    public void setCantidadOficial(Integer cantidadOficial) {
        this.cantidadOficial = cantidadOficial;
    }

    public BigDecimal getPrecioOficial() {
        return precioOficial;
    }

    public void setPrecioOficial(BigDecimal precioOficial) {
        this.precioOficial = precioOficial;
    }
}
