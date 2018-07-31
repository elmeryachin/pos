package bo.clync.pos.repository.acceso;

import bo.clync.pos.arquetipo.tablas.Conectado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by eyave on 28-10-17.
 */
public interface ConectadoRepository extends JpaRepository<Conectado, Integer> {


    @Query("SELECT o.token " +
            " FROM Conectado o, UsuarioAmbienteCredencial u " +
            "WHERE o.idUsuAmbCred = u.id " +
            "  AND u.usuario = :usuario" +
            "  AND o.fechaFin IS NULL")
    String buscarToken(@Param("usuario") String usuario);

    @Query("SELECT o.idUsuAmbCred " +
            " FROM Conectado o " +
            "WHERE o.token = :token " +
            "  AND o.fechaFin IS NULL")
    Integer obtenerIdUsuarioAmbienteCredencial(@Param("token") String token);

    Conectado findByTokenAndFechaFinIsNull(String token);
}
