package bo.clync.pos.utilitarios.articulo;

import bo.clync.pos.entity.Articulo;
import bo.clync.pos.model.articulo.obtener.ObjetoArticulo;

import java.util.Date;

/**
 * Created by eyave on 09-10-17.
 */
public class UtilsArticulo {

    public static Articulo convertirNuevoArticulo(ObjetoArticulo objeto, String operador){
        Articulo articulo = new Articulo();
        try {
            articulo.setCodigo(objeto.getCodigo().toUpperCase());
            articulo.setNombre(objeto.getNombre());
            articulo.setDescripcion(objeto.getDescripcion());
            articulo.setFechaAlta(new Date());
            articulo.setOperadorAlta(operador);
            return articulo;
        } finally {
            articulo = null;
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
                articulo.setCodigo(objeto.getCodigo().toUpperCase());
                control = true;
            }
            if(compare(articulo.getNombre(),objeto.getNombre())) {
                articulo.setNombre(objeto.getNombre());
                control = true;
            }
            if(objeto.getDescripcion()==null) {
                objeto.setDescripcion(" ");
            }
            if(compare(articulo.getDescripcion(),objeto.getDescripcion())) {
                articulo.setDescripcion(objeto.getDescripcion());
                control = true;
            }
            if(control) {
                articulo.setFechaActualizacion(new Date());
                articulo.setOperadorActualizacion(operador);
            }
            return control;
        } finally {
            articulo = null;
            objeto = null;
            operador = null;
        }
    }

}
