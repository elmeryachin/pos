package bo.clync.pos.arquetipo.objetos;

import bo.clync.pos.arquetipo.tablas.GesOperacion;

import java.util.List;

public class DiscoResponse {
    private String ruta;
    private String nombre;
    private List<GesOperacion> list;
    private String documento;
    private boolean respuesta;
    private String mensaje;
    
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

    public List<GesOperacion> getList() {
        return list;
    }

    public void setList(List<GesOperacion> list) {
        this.list = list;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
}
