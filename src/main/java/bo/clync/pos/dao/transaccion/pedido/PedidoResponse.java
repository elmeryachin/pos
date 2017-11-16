package bo.clync.pos.dao.transaccion.pedido;

/**
 * Created by eyave on 27-10-17.
 */
public class PedidoResponse {

    private PedidoObjeto PedidoObjeto;
    private boolean respuesta;
    private String mensaje;

    public PedidoObjeto getPedidoObjeto() {
        return PedidoObjeto;
    }

    public void setPedidoObjeto(PedidoObjeto pedidoObjeto) {
        this.PedidoObjeto = pedidoObjeto;
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
