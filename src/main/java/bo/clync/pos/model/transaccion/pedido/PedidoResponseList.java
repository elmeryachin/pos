package bo.clync.pos.model.transaccion.pedido;

import java.util.List;

/**
 * Created by eyave on 28-10-17.
 */
public class PedidoResponseList {
    private List<PedidoObjeto> list;
    private boolean respuesta;
    private String mensaje;

    public List<PedidoObjeto> getList() {
        return list;
    }

    public void setList(List<PedidoObjeto> list) {
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
