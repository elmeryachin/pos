/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.clync.pos.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author eyave
 */
@Entity
public class Transaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer idCiclo;
    private String codigoDominio;
    private String codigoValor;
    private String codigoValorPago;
    private Integer idUsuarioInicio;
    private Integer idAmbienteInicio;
    private Date fechaInicio;
    private Integer idUsuarioFin;
    private Integer idAmbienteFin;
    private Date fechaFin;
    private int cantidad;
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

    public Transaccion(Integer id) {
        this.setId(id);
    }

    public Transaccion(Integer id, Date fechaInicio, int cantidad, BigDecimal precio, Date fechaAlta, String operadorAlta) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getIdAmbienteInicio() {
        return idAmbienteInicio;
    }

    public void setIdAmbienteInicio(Integer idAmbienteInicio) {
        this.idAmbienteInicio = idAmbienteInicio;
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

    public Integer getIdAmbienteFin() {
        return idAmbienteFin;
    }

    public void setIdAmbienteFin(Integer idAmbienteFin) {
        this.idAmbienteFin = idAmbienteFin;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
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
