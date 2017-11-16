package bo.clync.pos.dao;

/**
 * Created by eyave on 07-11-17.
 */
public class MiExcepcion extends Exception {
    private boolean respuesta;
    private String mensaje;

    public MiExcepcion(String mensaje) {
        super();
        this.setMensaje(mensaje);
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
    @Override
    public String getMessage(){
        return this.mensaje;
    }
}
