package bo.clync.pos.dao;

/**
 * Created by eyave on 09-10-17.
 */
public class ServResponse {
    private boolean respuesta;
    private String mensaje;

    public ServResponse() {}

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
