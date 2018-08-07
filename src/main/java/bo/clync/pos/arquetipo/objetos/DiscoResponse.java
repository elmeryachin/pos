package bo.clync.pos.arquetipo.objetos;

public class DiscoResponse {

    private Byte[] array;
    private boolean respuesta;
    private String mensaje;

    public Byte[] getArray() {
        return array;
    }

    public void setArray(Byte[] array) {
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
}
