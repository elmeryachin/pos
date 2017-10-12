package bo.clync.pos.modulo.articulo.utils;

import bo.clync.pos.model.Articulo;
import bo.clync.pos.model.DetalleArticuloPrecio;
import bo.clync.pos.modulo.articulo.entity.obtener.ObjetoArticulo;

import javax.persistence.Id;
import java.util.Date;

/**
 * Created by eyave on 09-10-17.
 */
public class UtilsArticulo {

    public static Articulo convertirNuevoArticulo(ObjetoArticulo objeto, String operador){
        Articulo articulo = new Articulo();
        try {
            articulo.setCodigo(objeto.getCodigo());
            articulo.setNombre(objeto.getNombre());
            articulo.setDescripcion(objeto.getDescripcion());
            articulo.setFechaRegistro(new Date());
            articulo.setOperador(operador);
            return articulo;
        } finally {
            articulo = null;
            objeto = null;
            operador = null;
        }
    }

    public static DetalleArticuloPrecio convertirNuevoDetalleArticulo(ObjetoArticulo objeto, Integer idArticulo, String operador) {
        DetalleArticuloPrecio detalle = new DetalleArticuloPrecio();
        try {
            detalle.setIdArticulo(idArticulo);
            detalle.setPrecioKilo(objeto.getPrecioKilo());
            detalle.setPeso(objeto.getPeso());
            detalle.setPrecioZonaLibre(objeto.getPrecioZonaLibre());
            detalle.setPorcentajeGasto(objeto.getPorcentajeGasto());
            detalle.setPrecioCompra(objeto.getPrecioCompra());
            detalle.setPrecioVenta(objeto.getPrecioVenta());
            detalle.setPrecioMercado(objeto.getPrecioMercado());
            detalle.setFechaRegistro(new Date());
            detalle.setOperador(operador);
            return detalle;
        } finally {
            detalle = null;
            idArticulo = null;
            objeto = null;
            operador = null;
        }
    }

    public static boolean compare(Object a, Object b) {
        if((a==null && b!=null) || (b==null && a!=null)) return true;
        return !a.equals(b);
    }
    public static boolean convertirActualizarArticulo(Articulo articulo, ObjetoArticulo objeto, String operador){
        boolean control = false;
        try {
            if(compare(articulo.getCodigo(),objeto.getCodigo())) {
                articulo.setCodigo(objeto.getCodigo());
                control = true;
            }
            if(compare(articulo.getNombre(),objeto.getNombre())) {
                articulo.setNombre(objeto.getNombre());
                control = true;
            }
            if(compare(articulo.getDescripcion(),objeto.getDescripcion())) {
                articulo.setDescripcion(objeto.getDescripcion());
                control = true;
            }
            if(control) {
                articulo.setFechaActualizacion(new Date());
                articulo.setOperador(operador);
            }
            return control;
        } finally {
            articulo = null;
            objeto = null;
            operador = null;
        }
    }

    public static boolean validaActualizarDetalleArticulo(DetalleArticuloPrecio detalle, ObjetoArticulo objeto) {
        boolean control = false;
        try {
            if(detalle.getPrecioKilo().compareTo(objeto.getPrecioKilo())!=0) {
                control = true;
            }
            if(!detalle.getPeso().equals(objeto.getPeso())) {
                control = true;
            }
            if(detalle.getPrecioZonaLibre().compareTo(objeto.getPrecioZonaLibre())!=0) {
                control = true;
            }
            if(detalle.getPorcentajeGasto().compareTo(objeto.getPorcentajeGasto())!=0) {
                control = true;
            }
            if(detalle.getPrecioCompra().compareTo(objeto.getPrecioCompra())!=0) {
                control = true;
            }
            if(detalle.getPrecioVenta().compareTo(objeto.getPrecioVenta())!=0) {
                control = true;
            }
            if(detalle.getPrecioMercado().compareTo(objeto.getPrecioMercado())!=0) {
                control = true;
            }
            return control;
        } finally {
            detalle = null;
            objeto = null;
        }
    }

}
