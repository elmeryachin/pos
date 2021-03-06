package bo.clync.pos.arquetipo.objetos.generic;

public class UsuarioResponseMin {

    private String codigo;
    private String nombre;
    private boolean respuesta;
    private String mensaje;

    public UsuarioResponseMin(){}

    public UsuarioResponseMin(String codigo, String nombre){
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public boolean isRespuesta() {
        return respuesta;
    }

    public void setRespuesta(boolean respuesta) {
        this.respuesta = respuesta;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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

}
