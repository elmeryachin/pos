package bo.clync.pos.arquetipo.objetos;

import java.sql.Timestamp;

public class DiscoRequest {

    private Timestamp fechaInicio;
    private Timestamp fechaFinal;

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Timestamp getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Timestamp fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
}
