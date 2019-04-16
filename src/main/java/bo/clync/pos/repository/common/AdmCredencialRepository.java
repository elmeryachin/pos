package bo.clync.pos.repository.common;

import bo.clync.pos.arquetipo.dto.DatosUsuario;
import bo.clync.pos.arquetipo.tablas.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by eyave on 28-10-17.
 */
public interface AdmCredencialRepository extends JpaRepository<AdmCredencial, Integer> {


    @Query("SELECT new bo.clync.pos.arquetipo.dto.DatosUsuario(o.id, o.idUsuario, u.nombre , a.codigo, a.nombre, a.tipoAmbiente, o.contrasenia, o.token) " +
            " FROM AdmCredencial o, AdmUsuario u, PosAmbiente a " +
            "WHERE o.usuario=:usuario " +
            "  AND o.contrasenia = :contrasenia " +
            "  AND o.idUsuario = u.id " +
            "  AND o.codigoAmbiente = a.codigo " +
            "  AND o.fechaBaja is null " +
            "  AND a.fechaBaja is null " +
            "  AND u.fechaBaja is null")
    DatosUsuario getDatosUsuario(@Param("usuario") String usuario,
                                 @Param("contrasenia") String contrasenia);

    @Query("SELECT new bo.clync.pos.arquetipo.dto.DatosUsuario(o.id, o.idUsuario, u.nombre , a.codigo, a.nombre, a.tipoAmbiente, o.contrasenia, o.token) " +
            " FROM AdmCredencial o, AdmUsuario u, PosAmbiente a " +
            "WHERE o.token =:token " +
            "  AND o.idUsuario = u.id " +
            "  AND o.codigoAmbiente = a.codigo " +
            "  AND o.fechaBaja is null " +
            "  AND a.fechaBaja is null " +
            "  AND u.fechaBaja is null")
    DatosUsuario getDatosUsuario(@Param("token") String token);


    @Query("SELECT o.idUsuario " +
            " FROM AdmCredencial o " +
            "WHERE o.codigoAmbiente = :codigoAmbiente" +
            "  AND o.fechaBaja is null ")
    Long getIdUsuarioByCodigoAmbiente(@Param("codigoAmbiente") String codigoAmbiente);

    /*
    @Query("SELECT o.contrasenia, o.id, u.nombre, a.nombre, a.tipoAmbiente" +
            " FROM AdmCredencial o, AdmUsuario u, PosAmbiente a " +
            "WHERE o.usuario=:usuario " +
            "  AND o.idUsuario = u.id " +
            "  AND o.codigoAmbiente = a.codigo" +
            "  AND o.fechaBaja is null " +
            "  AND a.fechaBaja is null " +
            "  AND u.fechaBaja is null")
    Object getAutenticacion(@Param("usuario") String usuario);

    @Query("SELECT o.idUsuario, o.codigoAmbiente " +        // EDITADO
            " FROM AdmCredencial o " +
            "WHERE o.token=:token " +
            "  AND o.fechaBaja is null")
    Object origenCredencial(@Param("token") String token);

    @Query("SELECT o.codigoAmbiente " +
            " FROM AdmCredencial o, Conectado cc " +
            "WHERE o.id = cc.idUsuAmbCred " +
            "  AND cc.token = :token " +
            "  AND o.fechaBaja is null " +
            "  AND cc.fechaFin is null ")
    String getCodigoAmbienteByToken (@Param("token") String token);

    @Query("SELECT o.idUsuario, o.codigoAmbiente " +
            " FROM AdmCredencial o, Conectado cc " +
            "WHERE o.id = cc.idUsuAmbCred " +
            "  AND cc.token = :token " +
            "  AND o.fechaBaja is null " +
            "  AND cc.fechaFin is null ")
    Object getIdUsuarioByToken(@Param("token") String token);

*/


}
