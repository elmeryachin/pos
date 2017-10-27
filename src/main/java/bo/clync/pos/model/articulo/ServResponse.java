package bo.clync.pos.model.articulo;

/**
 * Created by eyave on 09-10-17.
 */
public class ServResponse {
    private boolean respuesta;
    private String mensaje;

    public ServResponse(boolean respuesta, String mensaje) {
        this.respuesta = respuesta;
        this.mensaje = mensaje;
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
