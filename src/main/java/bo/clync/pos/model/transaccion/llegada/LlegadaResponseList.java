package bo.clync.pos.model.transaccion.llegada;

import java.util.List;

/**
 * Created by eyave on 06-11-17.
 */
public class LlegadaResponseList {
    private List<LlegadaObjeto> list;
    private boolean respuesta;
    private String mensaje;

    public List<LlegadaObjeto> getList() {
        return list;
    }

    public void setList(List<LlegadaObjeto> list) {
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
