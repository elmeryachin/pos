/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.clync.pos.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author eyave
 */
@Entity
@Table(name = "sub_dominio")
@NamedQueries({
    @NamedQuery(name = "SubDominio.findAll", query = "SELECT s FROM SubDominio s")})
public class SubDominio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "codigo_sub_dominio")
    private String codigoSubDominio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "codigo_dominio", referencedColumnName = "dominio")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Dominio codigoDominio;


    public SubDominio() {
    }

    public SubDominio(String codigoSubDominio) {
        this.codigoSubDominio = codigoSubDominio;
    }

    public SubDominio(String codigoSubDominio, String nombre, String descripcion) {
        this.codigoSubDominio = codigoSubDominio;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getCodigoSubDominio() {
        return codigoSubDominio;
    }

    public void setCodigoSubDominio(String codigoSubDominio) {
        this.codigoSubDominio = codigoSubDominio;
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

    public Dominio getCodigoDominio() {
        return codigoDominio;
    }

    public void setCodigoDominio(Dominio codigoDominio) {
        this.codigoDominio = codigoDominio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoSubDominio != null ? codigoSubDominio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubDominio)) {
            return false;
        }
        SubDominio other = (SubDominio) object;
        if ((this.codigoSubDominio == null && other.codigoSubDominio != null) || (this.codigoSubDominio != null && !this.codigoSubDominio.equals(other.codigoSubDominio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.clync.pos.model.SubDominio[ codigoSubDominio=" + codigoSubDominio + " ]";
    }
    
}
