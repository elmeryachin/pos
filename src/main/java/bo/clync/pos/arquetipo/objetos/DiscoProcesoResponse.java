package bo.clync.pos.arquetipo.objetos;

import java.util.List;

public class DiscoProcesoResponse {

    private List<ProcesoResumen> list;
    private boolean respuesta;
    private String mensaje;


    public List<ProcesoResumen> getList() {
        return list;
    }

    public void setList(List<ProcesoResumen> list) {
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
