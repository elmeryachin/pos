package bo.clync.pos.dao.transaccion.pedido;

/**
 * Created by eyave on 16-11-17.
 */
public class UsuarioRequest {
    private String codigo;
    private String nombre;
    private String direccion;
    private String telefono;

    public UsuarioRequest() {}

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
