package bo.clync.pos.dao.inventario;

import bo.clync.pos.dao.ResumenExistencia;

import java.util.List;

/**
 * Created by eyave on 03-12-17.
 */
public class ExistenciaResponseList {
    private List<ResumenExistencia> list;
    private boolean respuesta;
    private String mensaje;

    public List<ResumenExistencia> getList() {
        return list;
    }

    public void setList(List<ResumenExistencia> list) {
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
