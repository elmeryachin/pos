/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.clync.pos.arquetipo.tablas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author eyave
 */
@Entity
public class DetalleTransaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id//@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String idTransaccion;
    private String codigoArticulo;
    private Integer cantidad;
    private BigDecimal precio;
    private BigDecimal precioSistema;
    private String observacion;
    private Date fechaAlta;
    private String operadorAlta;
    private Date fechaActualizacion;
    private String operadorActualizacion;
    private Date fechaBaja;
    private String operadorBaja;
    private Integer cantidadOficial;
    private BigDecimal precioOficial;

    public DetalleTransaccion() {
    }

    public DetalleTransaccion(String id) {
        this.id = id;
    }

    public DetalleTransaccion(String id, BigDecimal precioSistema, int cantidad, BigDecimal precio, String operadorBaja) {
        this.id = id;
        this.precioSistema = precioSistema;
        this.cantidad = cantidad;
        this.precio = precio;
        this.operadorBaja = operadorBaja;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getPrecioSistema() {
        return precioSistema;
    }

    public void setPrecioSistema(BigDecimal precioSistema) {
        this.precioSistema = precioSistema;
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

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getOperadorAlta() {
        return operadorAlta;
    }

    public void setOperadorAlta(String operadorAlta) {
        this.operadorAlta = operadorAlta;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getOperadorActualizacion() {
        return operadorActualizacion;
    }

    public void setOperadorActualizacion(String operadorActualizacion) {
        this.operadorActualizacion = operadorActualizacion;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getOperadorBaja() {
        return operadorBaja;
    }

    public void setOperadorBaja(String operadorBaja) {
        this.operadorBaja = operadorBaja;
    }

    public String getCodigoArticulo() {
        return codigoArticulo;
    }

    public void setCodigoArticulo(String idArticulo) {
        this.codigoArticulo = idArticulo;
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleTransaccion)) {
            return false;
        }
        DetalleTransaccion other = (DetalleTransaccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.clync.pos.entity.DetalleTransaccion[ id=" + id + " ]";
    }
}
