package bo.clync.pos.arquetipo.objetos.articulo.obtener;

import bo.clync.pos.arquetipo.tablas.PosArticulo;

import java.math.BigDecimal;

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
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private BigDecimal precioMercado;

    public ObjetoArticulo(){}

    public ObjetoArticulo(PosArticulo posArticulo){
        this.codigo = posArticulo.getCodigo();
        this.nombre = posArticulo.getNombre();
        this.descripcion = posArticulo.getDescripcion();
        this.precioKilo = posArticulo.getPrecioKilo();
        this.peso = posArticulo.getPeso();
        this.precioZonaLibre = posArticulo.getPrecioZonaLibre();
        this.porcentajeGasto = posArticulo.getPorcentajeGasto();
        this.precioCompra = posArticulo.getPrecioCompra();
        this.precioVenta = posArticulo.getPrecioVenta();
        this.precioMercado = posArticulo.getPrecioMercado();
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
