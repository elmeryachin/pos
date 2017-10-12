/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.clync.pos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
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
@Table(name = "detalle_articulo_precio")
@NamedQueries({
    @NamedQuery(name = "DetalleArticuloPrecio.findAll", query = "SELECT d FROM DetalleArticuloPrecio d")})
public class DetalleArticuloPrecio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_kilo")
    private BigDecimal precioKilo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "peso")
    private BigDecimal peso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_zona_libre")
    private BigDecimal precioZonaLibre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje_gasto")
    private BigDecimal porcentajeGasto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_compra")
    private BigDecimal precioCompra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_venta")
    private BigDecimal precioVenta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_mercado")
    private BigDecimal precioMercado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "fecha_baja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaBaja;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "operador")
    private String operador;
    //@JoinColumn(name = "id_articulo", referencedColumnName = "id")
    private Integer idArticulo;

    public DetalleArticuloPrecio() {
    }

    public DetalleArticuloPrecio(Integer id) {
        this.id = id;
    }

    public DetalleArticuloPrecio(Integer id, BigDecimal precioKilo, BigDecimal peso, BigDecimal precioZonaLibre, BigDecimal porcentajeGasto, BigDecimal precioCompra, BigDecimal precioVenta, BigDecimal precioMercado, Date fechaRegistro, String operador, Integer idArticulo) {
        this.id = id;
        this.precioKilo = precioKilo;
        this.peso = peso;
        this.precioZonaLibre = precioZonaLibre;
        this.porcentajeGasto = porcentajeGasto;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.precioMercado = precioMercado;
        this.fechaRegistro = fechaRegistro;
        this.operador = operador;
        this.idArticulo = idArticulo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
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
        if (!(object instanceof DetalleArticuloPrecio)) {
            return false;
        }
        DetalleArticuloPrecio other = (DetalleArticuloPrecio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.clync.pos.model.DetalleArticuloPrecio[ id=" + id + " ]";
    }
    
}
