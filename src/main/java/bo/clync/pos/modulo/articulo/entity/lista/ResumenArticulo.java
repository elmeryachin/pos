package bo.clync.pos.modulo.articulo.entity.lista;

/**
 * Created by eyave on 09-10-17.
 */
public class ResumenArticulo {
    private String codigo;
    private String nombre;
    private String descripcion;

    public ResumenArticulo(String codigo, String nombre, String descripcion) {
        this.codigo = codigo;
        this.setNombre(nombre);
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
