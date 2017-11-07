package bo.clync.pos.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by eyave on 28-10-17.
 */
@Entity
public class Conectado {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer idUsuAmbCred;
    private String token;
    private Date fechaInicio;
    private Date fechaFin;

    public Conectado() {}

    public Conectado(Integer idUsuAmbCred, String token, Date fechaInicio) {
        this.idUsuAmbCred = idUsuAmbCred;
        this.token = token;
        this.fechaInicio = fechaInicio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUsuAmbCred() {
        return idUsuAmbCred;
    }

    public void setIdUsuAmbCred(Integer idUsuAmbCred) {
        this.idUsuAmbCred = idUsuAmbCred;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

}
