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
            detalle.setGasto(objeto.getGasto());
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


    public static boolean convertirActualizarArticulo(Articulo articulo, ObjetoArticulo objeto, String operador){
        boolean control = false;
        try {
            if(!articulo.getCodigo().equals(objeto.getCodigo())) {
                articulo.setCodigo(objeto.getCodigo());
                control = true;
            }
            if(!articulo.getNombre().equals(objeto.getNombre())) {
                articulo.setNombre(objeto.getNombre());
                control = true;
            }
            if(!articulo.getDescripcion().equals(objeto.getDescripcion())) {
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
            if(!detalle.getPrecioKilo().equals(objeto.getPrecioKilo())) {
                control = true;
            }
            if(!detalle.getPeso().equals(objeto.getPeso())) {
                control = true;
            }
            if(!detalle.getPrecioZonaLibre().equals(objeto.getPrecioZonaLibre())) {
                control = true;
            }
            if(!detalle.getPorcentajeGasto().equals(objeto.getPorcentajeGasto())) {
                control = true;
            }
            if(!detalle.getGasto().equals(objeto.getGasto())) {
                control = true;
            }
            if(!detalle.getPrecioCompra().equals(objeto.getPrecioCompra())) {
                control = true;
            }
            if(!detalle.getPrecioVenta().equals(objeto.getPrecioVenta())) {
                control = true;
            }
            if(!detalle.getPrecioMercado().equals(objeto.getPrecioMercado())) {
                control = true;
            }
            return control;
        } finally {
            detalle = null;
            objeto = null;
        }
    }

}
