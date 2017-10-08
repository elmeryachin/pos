package bo.clync.pos.entity;

/**
 * Created by eyave on 08-10-17.
 */
public class Salida {

    private Object  entity;
    private boolean respuesta;
    private String  mensaje;

    public Salida(){}

    public Salida(Object entity, boolean respuesta, String mensaje){
        this.entity     = entity;
        this.respuesta  = respuesta;
        this.mensaje    = mensaje;
    }

    public Salida(Object entity){
        if(entity == null) {
            this.mensaje    = "No se encontro el registro";
        } else {
            this.entity = entity;
            this.respuesta  = true;
            this.mensaje    = "";
        }
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
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
