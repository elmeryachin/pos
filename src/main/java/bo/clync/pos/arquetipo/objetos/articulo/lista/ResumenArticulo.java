package bo.clync.pos.arquetipo.objetos.articulo.lista;

import bo.clync.pos.arquetipo.tablas.PosArticulo;

/**
 * Created by eyave on 09-10-17.
 */
public class ResumenArticulo {
    private String codigo;
    private String nombre;
    private String descripcion;

    public ResumenArticulo(PosArticulo posArticulo) {
        this.codigo = posArticulo.getCodigo();
        this.nombre = posArticulo.getNombre();
        this.descripcion = posArticulo.getDescripcion();
    }

    public ResumenArticulo ( String codigo, String nombre, String descripcion){
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
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
