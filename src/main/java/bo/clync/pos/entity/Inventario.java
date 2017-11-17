/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.clync.pos.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author eyave
 */
@Entity
public class Inventario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String codigoAmbiente;
    private String codigoArticulo;
    private Integer existencia;
    private Integer porLlegar;
    private Integer porEntregar;
    private Integer porRecibir;

    private Date fechaAlta;
    private String operadorAlta;
    private Date fechaActualizacion;
    private String operadorActualizacion;
    private Date fechaBaja;
    private String operadorBaja;


    public Inventario() {
    }

    public Inventario(Integer id) {
        this.id = id;
    }

    public Inventario(Integer id, Integer existencia, Date fechaAlta, String operadorBaja) {
        this.id = id;
        this.existencia = existencia;
        this.fechaAlta = fechaAlta;
        this.operadorBaja = operadorBaja;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExistencia() {
        return existencia;
    }

    public void setExistencia(Integer existencia) {
        this.existencia = existencia;
    }

    public Integer getPorLlegar() {
        return porLlegar;
    }

    public void setPorLlegar(Integer porLlegar) {
        this.porLlegar = porLlegar;
    }

    public Integer getPorEntregar() {
        return porEntregar;
    }

    public void setPorEntregar(Integer porEntregar) {
        this.porEntregar = porEntregar;
    }

    public Integer getPorRecibir() {
        return porRecibir;
    }

    public void setPorRecibir(int porRecibir) {
        this.porRecibir = porRecibir;
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

    public String getCodigoAmbiente() {
        return codigoAmbiente;
    }

    public void setCodigoAmbiente(String idAmbiente) {
        this.codigoAmbiente = idAmbiente;
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
        if (!(object instanceof Inventario)) {
            return false;
        }
        Inventario other = (Inventario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.clync.pos.entity.Inventario[ id=" + id + " ]";
    }


}
