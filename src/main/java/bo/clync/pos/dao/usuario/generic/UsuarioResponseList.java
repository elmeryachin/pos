package bo.clync.pos.dao.usuario.generic;


import java.util.List;

public class UsuarioResponseList {
    private List<UsuarioResponseMin> list;
    private boolean respuesta;
    private String mensaje;

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

    public List<UsuarioResponseMin> getList() {
        return list;
    }

    public void setList(List<UsuarioResponseMin> list) {
        this.list = list;
    }
}
