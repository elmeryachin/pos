package bo.clync.pos.model.articulo.obtener;

/**
 * Created by eyave on 09-10-17.
 */

public class ServObtenerResponse {

    private ObjetoArticulo articulo;
    private boolean respuesta;
    private String mensaje;


    public ServObtenerResponse(){}

    public ServObtenerResponse(ObjetoArticulo articulo, boolean respuesta, String mensaje){
        this.articulo = articulo;
        this.respuesta = respuesta;
        this.mensaje = mensaje;
    }

    public ObjetoArticulo getArticulo() {
        return articulo;
    }

    public void setArticulo(ObjetoArticulo articulo) {
        this.articulo = articulo;
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
