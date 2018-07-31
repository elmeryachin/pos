package bo.clync.pos.arquetipo.objetos.articulo;

import bo.clync.pos.arquetipo.objetos.Resumen;

import java.util.List;

/**
 * Created by eyave on 28-10-17.
 */
public class ArticuloResponseList {
    private List<Resumen> list;
    private boolean respuesta;
    private String mensaje;

    public List<Resumen> getList() {
        return list;
    }

    public void setList(List<Resumen> list) {
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
