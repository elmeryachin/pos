package bo.clync.pos.arquetipo.objetos;

import bo.clync.pos.arquetipo.tablas.AbcOperaciones;

import java.util.List;

public class DiscoResponse {
    private String ruta;
    private String nombre;
    private List<AbcOperaciones> list;
    private byte[] array;
    private boolean respuesta;
    private String mensaje;

    public byte[] getArray() {
        return array;
    }

    public void setArray(byte[] array) {
        this.array = array;
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

    public List<AbcOperaciones> getList() {
        return list;
    }

    public void setList(List<AbcOperaciones> list) {
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
}
