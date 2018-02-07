package bo.clync.pos.dao.inventario;

/**
 * Created by eyave on 06-02-18.
 */
public class Sucursales {
    private String codigo;
    private String nombre;
    private String tipoAmbiente;

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

    public String getTipoAmbiente() {
        return tipoAmbiente;
    }

    public void setTipoAmbiente(String tipoAmbiente) {
        this.tipoAmbiente = tipoAmbiente;
    }
}
