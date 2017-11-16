package bo.clync.pos.repository.acceso;

import bo.clync.pos.entity.Conectado;
import bo.clync.pos.dao.ServResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * Created by eyave on 28-10-17.
 */
public interface ConectadoRepository extends JpaRepository<Conectado, Integer> {


    @Query("SELECT o.token " +
            " FROM Conectado o, UsuarioAmbienteCredencial u " +
            "WHERE o.idUsuAmbCred = u.id " +
            "  AND u.usuario = :usuario" +
            "  AND o.fechaFin IS NULL")
    public String buscarToken(@Param("usuario") String usuario);

    @Query("SELECT o.idUsuAmbCred " +
            " FROM Conectado o " +
            "WHERE o.token = :token " +
            "  AND o.fechaFin IS NULL")
    public Integer obtenerIdUsuarioAmbienteCredencial(@Param("token") String token);

    @Query("UPDATE Conectado o" +
            "  SET o.fechaFin =:fechaFin " +
            "WHERE o.token = :token " +
            "  AND o.fechaFin IS NULL")
    public ServResponse salir(@Param("fechaFin") Date fechaFin, @Param("token") String token);
}
