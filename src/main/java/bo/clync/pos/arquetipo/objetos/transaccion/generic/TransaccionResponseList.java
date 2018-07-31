package bo.clync.pos.arquetipo.objetos.transaccion.generic;

import java.util.List;

public class TransaccionResponseList {
    private List<TransaccionObjeto> list;
    private boolean respuesta;
    private String mensaje;

    public List<TransaccionObjeto> getList() {
        return list;
    }

    public void setList(List<TransaccionObjeto> list) {
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
