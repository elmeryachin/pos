package bo.clync.pos.model.articulo.lista;

import bo.clync.pos.entity.Articulo;

/**
 * Created by eyave on 09-10-17.
 */
public class ResumenArticulo {
    private String codigo;
    private String nombre;
    private String descripcion;

    public ResumenArticulo(Articulo articulo) {
        this.codigo = articulo.getCodigo();
        this.nombre = articulo.getNombre();
        this.descripcion = articulo.getDescripcion();
    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
