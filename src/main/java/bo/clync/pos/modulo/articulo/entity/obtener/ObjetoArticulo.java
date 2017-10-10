package bo.clync.pos.modulo.articulo.entity.obtener;

import bo.clync.pos.model.Articulo;
import bo.clync.pos.model.DetalleArticuloPrecio;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by eyave on 09-10-17.
 */
public class ObjetoArticulo {
    private String codigo;
    private String nombre;
    private String descripcion;

    private BigDecimal precioKilo;
    private BigDecimal peso;
    private BigDecimal precioZonaLibre;
    private BigDecimal porcentajeGasto;
    private BigDecimal gasto;//Es el calculo enre precioZonaLibre * porcentajeGasto
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private BigDecimal precioMercado;

    public ObjetoArticulo(){}

    public ObjetoArticulo(Articulo articulo, DetalleArticuloPrecio detalle){
        this.codigo = articulo.getCodigo();
        this.nombre = articulo.getNombre();
        this.descripcion = articulo.getDescripcion();

        this.precioKilo = detalle.getPrecioKilo();
        this.peso = detalle.getPeso();
        this.precioZonaLibre = detalle.getPrecioZonaLibre();
        this.porcentajeGasto = detalle.getPorcentajeGasto();
        this.gasto = detalle.getGasto();
        this.precioCompra = detalle.getPrecioCompra();
        this.precioVenta = detalle.getPrecioVenta();
        this.precioMercado = detalle.getPrecioMercado();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioKilo() {
        return precioKilo;
    }

    public void setPrecioKilo(BigDecimal precioKilo) {
        this.precioKilo = precioKilo;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getPrecioZonaLibre() {
        return precioZonaLibre;
    }

    public void setPrecioZonaLibre(BigDecimal precioZonaLibre) {
        this.precioZonaLibre = precioZonaLibre;
    }

    public BigDecimal getPorcentajeGasto() {
        return porcentajeGasto;
    }

    public void setPorcentajeGasto(BigDecimal porcentajeGasto) {
        this.porcentajeGasto = porcentajeGasto;
    }

    public BigDecimal getGasto() {
        return gasto;
    }

    public void setGasto(BigDecimal gasto) {
        this.gasto = gasto;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public BigDecimal getPrecioMercado() {
        return precioMercado;
    }

    public void setPrecioMercado(BigDecimal precioMercado) {
        this.precioMercado = precioMercado;
    }
}
