package bo.clync.pos.arquetipo.objetos.inventario;

import java.util.List;

/**
 * Created by eyave on 06-02-18.
 */
public class SucursalesResponseList {
    private List<Sucursales> list;
    private boolean respuesta;
    private String mensaje;

    public List<Sucursales> getList() {
        return list;
    }

    public void setList(List<Sucursales> list) {
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
