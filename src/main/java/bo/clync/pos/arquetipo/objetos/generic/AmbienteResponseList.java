package bo.clync.pos.arquetipo.objetos.generic;

import java.util.List;

public class AmbienteResponseList {

    private List<AmbienteResponseMin> list;
    private boolean respuesta;
    private String mensaje;

    public List<AmbienteResponseMin> getList() {
        return list;
    }

    public void setList(List<AmbienteResponseMin> list) {
        this.list = list;
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
}
