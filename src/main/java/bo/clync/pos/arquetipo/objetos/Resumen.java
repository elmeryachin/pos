package bo.clync.pos.arquetipo.objetos;

/**
 * Created by eyave on 29-10-17.
 */
public class Resumen {
    private String codigo;
    private String nombre;
    public Resumen(){}
    public Resumen(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }
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

    public void obtenerMonto(Integer monto) {
        this.nombre = monto==null?"0":String.valueOf(monto);
    }
}
