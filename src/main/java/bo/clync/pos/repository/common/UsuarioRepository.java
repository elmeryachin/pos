package bo.clync.pos.repository.common;

import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin;
import bo.clync.pos.arquetipo.tablas.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by eyave on 28-10-17.
 */

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByCodigoAndCodigoValorUsuarioAndFechaBajaIsNull(String codigo, String codigoValorUsuario);

    @Query("SELECT o.id " +
            " FROM Usuario o " +
            "WHERE o.codigo = :codigo " +
            "  AND o.codigoValorUsuario=:codigoValorUsuario " +
            "  AND o.codigoAmbiente=:codigoAmbiente" +
            "  AND o.fechaBaja IS NULL")
    Long getIdUsuarioConAmbiente(@Param("codigo") String codigo,
                                    @Param("codigoValorUsuario") String codigoValorUsuario,
                                    @Param("codigoAmbiente") String codigoAmbiente);

    @Query("SELECT o.id " +
            " FROM Usuario o " +
            "WHERE o.codigo = :codigo " +
            "  AND o.codigoValorUsuario=:codigoValorUsuario " +
            "  AND o.fechaBaja IS NULL")
    Long getIdUsuarioCliente(@Param("codigo") String codigo,
                                    @Param("codigoValorUsuario") String codigoValorUsuario);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin(o.codigo, o.nombre) " +
            " FROM Usuario o " +
            "WHERE o.codigo LIKE :codigo" +
            "  AND o.codigoValorUsuario=:codigoValorUsuario " +
            "  AND o.codigoAmbiente=:codigoAmbiente " +
            "  AND o.fechaBaja IS NULL")
    List<UsuarioResponseMin> getListaUsuarioResumenPorPatron(@Param("codigo") String codigo,
                                                             @Param("codigoValorUsuario") String codigoValorUsuario,
                                                             @Param("codigoAmbiente") String codigoAmbiente);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin(o.codigo, o.nombre) " +
            " FROM Usuario o " +
            "WHERE o.codigoValorUsuario=:codigoValorUsuario " +
            "  AND o.codigoAmbiente=:codigoAmbiente " +
            "  AND o.fechaBaja IS NULL")
    List<UsuarioResponseMin> getListaUsuarioResumen(@Param("codigoValorUsuario") String codigoValorUsuario,
                                                    @Param("codigoAmbiente") String codigoAmbiente);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin(o.codigo, o.nombre) " +
            " FROM Usuario o " +
            "WHERE o.codigoValorUsuario=:codigoValorUsuario " +
            "  AND o.fechaBaja IS NULL")
    List<UsuarioResponseMin> getTodosUsuarioResumen(@Param("codigoValorUsuario") String codigoValorUsuario);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin(o.codigo, o.nombre) " +
            " FROM Usuario o " +
            "WHERE o.codigo LIKE :codigo" +
            "  AND o.codigoValorUsuario=:codigoValorUsuario " +
            "  AND o.fechaBaja IS NULL")
    List<UsuarioResponseMin> getTodosUsuarioResumenPorPatron(@Param("codigo") String codigo,
                                                             @Param("codigoValorUsuario") String codigoValorUsuario);

    @Query("SELECT count(id) " +
            " FROM Usuario o ")
    Integer maximoIdRegistro();

}
