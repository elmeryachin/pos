package bo.clync.pos.model.transaccion.llegada;

/**
 * Created by eyave on 06-11-17.
 */
public class LlegadaResponse {

    private LlegadaObjeto llegadaObjeto;
    private boolean respuesta;
    private String mensaje;

    public LlegadaObjeto getLlegadaObjeto() {
        return llegadaObjeto;
    }

    public void setLlegadaObjeto(LlegadaObjeto llegadaObjeto) {
        this.llegadaObjeto = llegadaObjeto;
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
