package bo.clync.pos.model.transaccion.pedido;

import java.util.Date;

/**
 * Created by eyave on 28-10-17.
 */
public class PedidoResponseInit {
    private Integer nroMovimiento;
    private String fechaMovimiento;
    private boolean respuesta;
    private String mensaje;


    public Integer getNroMovimiento() {
        return nroMovimiento;
    }

    public void setNroMovimiento(Integer nroMovimiento) {
        this.nroMovimiento = nroMovimiento;
    }

    public String getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(String fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
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
