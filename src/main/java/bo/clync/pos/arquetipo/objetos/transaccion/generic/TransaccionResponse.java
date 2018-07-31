package bo.clync.pos.arquetipo.objetos.transaccion.generic;

public class TransaccionResponse {


    private TransaccionObjeto transaccionObjeto;
    private boolean respuesta;
    private String mensaje;

    public TransaccionObjeto getTransaccionObjeto() {
        return transaccionObjeto;
    }

    public void setTransaccionObjeto(TransaccionObjeto transaccionObjeto) {
        this.transaccionObjeto = transaccionObjeto;
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
