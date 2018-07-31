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
public class Transaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private Integer idCiclo;
    private String codigoDominio;
    private Integer nroMovimiento;
    private String codigoValor;
    private String codigoValorPago;
    private Integer idUsuarioInicio;
    private String codigoAmbienteInicio;
    private Date fechaInicio;
    private Integer idUsuarioFin;
    private String codigoAmbienteFin;
    private Date fechaFin;
    private Integer cantidad;
    private BigDecimal precio;
    private String observacion;
    private Date fechaAlta;
    private String operadorAlta;
    private Date fechaActualizacion;
    private String operadorActualizacion;
    private Date fechaBaja;
    private String operadorBaja;

    public Transaccion() {
    }

    public Transaccion(String id) {
        this.setId(id);
    }

    public Transaccion(String id, Date fechaInicio, Integer cantidad, BigDecimal precio, Date fechaAlta, String operadorAlta) {
        this.setId(id);
        this.setFechaInicio(fechaInicio);
        this.setCantidad(cantidad);
        this.setPrecio(precio);
        this.setFechaAlta(fechaAlta);
        this.setOperadorAlta(operadorAlta);
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(Integer idCiclo) {
        this.idCiclo = idCiclo;
    }

    public String getCodigoDominio() {
        return codigoDominio;
    }

    public void setCodigoDominio(String codigoDominio) {
        this.codigoDominio = codigoDominio;
    }

    public String getCodigoValor() {
        return codigoValor;
    }

    public void setCodigoValor(String codigoValor) {
        this.codigoValor = codigoValor;
    }

    public String getCodigoValorPago() {
        return codigoValorPago;
    }

    public void setCodigoValorPago(String codigoValorPago) {
        this.codigoValorPago = codigoValorPago;
    }

    public Integer getIdUsuarioInicio() {
        return idUsuarioInicio;
    }

    public void setIdUsuarioInicio(Integer idUsuarioInicio) {
        this.idUsuarioInicio = idUsuarioInicio;
    }

    public String getCodigoAmbienteInicio() {
        return codigoAmbienteInicio;
    }

    public void setCodigoAmbienteInicio(String idAmbienteInicio) {
        this.codigoAmbienteInicio = idAmbienteInicio;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Integer getIdUsuarioFin() {
        return idUsuarioFin;
    }

    public void setIdUsuarioFin(Integer idUsuarioFin) {
        this.idUsuarioFin = idUsuarioFin;
    }

    public String getCodigoAmbienteFin() {
        return codigoAmbienteFin;
    }

    public void setCodigoAmbienteFin(String idAmbienteFin) {
        this.codigoAmbienteFin = idAmbienteFin;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
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

    public Integer getNroMovimiento() {
        return nroMovimiento;
    }

    public void setNroMovimiento(Integer nroMovimiento) {
        this.nroMovimiento = nroMovimiento;
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
        if (!(object instanceof Transaccion)) {
            return false;
        }
        Transaccion other = (Transaccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.clync.pos.entity.Transaccion[ id=" + id + " ]";
    }

}
