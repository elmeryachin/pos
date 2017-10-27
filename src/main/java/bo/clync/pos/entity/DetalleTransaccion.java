/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.clync.pos.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author eyave
 */
@Entity
public class DetalleTransaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private BigDecimal precioSistema;
    private int cantidad;
    private BigDecimal precio;
    private String observacion;
    private Date fechaAlta;
    private String operadorAlta;
    private Date fechaActualizacion;
    private String operadorActualizacion;
    private Date fechaBaja;
    private String operadorBaja;
    private Integer idArticulo;
    private Integer idTransaccion;

    public DetalleTransaccion() {
    }

    public DetalleTransaccion(Integer id) {
        this.id = id;
    }

    public DetalleTransaccion(Integer id, BigDecimal precioSistema, int cantidad, BigDecimal precio, String operadorBaja) {
        this.id = id;
        this.precioSistema = precioSistema;
        this.cantidad = cantidad;
        this.precio = precio;
        this.operadorBaja = operadorBaja;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrecioSistema() {
        return precioSistema;
    }

    public void setPrecioSistema(BigDecimal precioSistema) {
        this.precioSistema = precioSistema;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
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

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public Integer getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Integer idTransaccion) {
        this.idTransaccion = idTransaccion;
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
