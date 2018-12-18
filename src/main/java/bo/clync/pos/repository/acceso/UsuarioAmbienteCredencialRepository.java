package bo.clync.pos.repository.acceso;

import bo.clync.pos.arquetipo.tablas.UsuarioAmbienteCredencial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by eyave on 28-10-17.
 */
public interface UsuarioAmbienteCredencialRepository extends JpaRepository<UsuarioAmbienteCredencial, Integer> {

    @Query("SELECT o.contrasenia, o.id, u.nombre, a.nombre, a.tipoAmbiente" +
            " FROM UsuarioAmbienteCredencial o, Usuario u, Ambiente a " +
            "WHERE o.usuario=:usuario " +
            "  AND o.idUsuario = u.id " +
            "  AND o.codigoAmbiente = a.codigo" +
            "  AND o.fechaBaja is null " +
            "  AND a.fechaBaja is null " +
            "  AND u.fechaBaja is null")
    public Object getAutenticacion(@Param("usuario") String usuario);

    @Query("SELECT o.idUsuario, o.codigoAmbiente " +
            " FROM UsuarioAmbienteCredencial o " +
            "WHERE o.id=:id " +
            "  AND o.fechaBaja is null")
    public Object origenCredencial(@Param("id") Integer id);

    @Query("SELECT o.codigoAmbiente " +
            " FROM UsuarioAmbienteCredencial o, Conectado cc " +
            "WHERE o.id = cc.idUsuAmbCred " +
            "  AND cc.token = :token " +
            "  AND o.fechaBaja is null " +
            "  AND cc.fechaFin is null ")
    public String getCodigoAmbienteByToken (@Param("token") String token);

    @Query("SELECT o.idUsuario, o.codigoAmbiente " +
            " FROM UsuarioAmbienteCredencial o, Conectado cc " +
            "WHERE o.id = cc.idUsuAmbCred " +
            "  AND cc.token = :token " +
            "  AND o.fechaBaja is null " +
            "  AND cc.fechaFin is null ")
    public Object getIdUsuarioByToken(@Param("token") String token);

    @Query("SELECT o.idUsuario " +
            " FROM UsuarioAmbienteCredencial o " +
            "WHERE o.codigoAmbiente = :codigoAmbiente" +
            "  AND o.fechaBaja is null ")
    public Long getIdUsuarioByCodigoAmbiente(@Param("codigoAmbiente") String codigoAmbiente);


}
