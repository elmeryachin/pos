package bo.clync.pos.arquetipo.objetos.articulo.lista;

import java.util.List;

/**
 * Created by eyave on 09-10-17.
 */
public class ServListaResponse {

    private List<ResumenArticulo> lista;
    private boolean         respuesta;
    private String          mensaje;

    public ServListaResponse(){}

    public ServListaResponse(List<ResumenArticulo> lista, boolean respuesta, String mensaje){
        this.lista = lista;
        this.respuesta = respuesta;
        this.mensaje   = mensaje;
    }

    public ServListaResponse(List<ResumenArticulo> lista, boolean respuesta){
        this.lista = lista;
        this.respuesta = respuesta;
    }

    public List<ResumenArticulo> getLista() {
        return lista;
    }

    public void setLista(List<ResumenArticulo> lista) {
        this.lista = lista;
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
