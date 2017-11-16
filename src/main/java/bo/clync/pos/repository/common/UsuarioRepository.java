package bo.clync.pos.repository.common;

import bo.clync.pos.entity.Usuario;
import bo.clync.pos.dao.Resumen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by eyave on 28-10-17.
 */

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByCodigoAndCodigoValorUsuarioAndCodigoAmbienteAndFechaBajaIsNull(String codigo, String codigoValorUsuario, String codigoAmbiente);

    @Query("SELECT o.id " +
            " FROM Usuario o " +
            "WHERE o.codigo = :codigo " +
            "  AND o.codigoValorUsuario=:codigoValorUsuario " +
            "  AND o.codigoAmbiente=:codigoAmbiente" +
            "  AND o.fechaBaja IS NULL")
    Integer getIdUsuarioProveedor(@Param("codigo") String codigo,
                                         @Param("codigoValorUsuario") String codigoValorUsuario,
                                         @Param("codigoAmbiente") String codigoAmbiente);

    @Query("SELECT new bo.clync.pos.dao.Resumen(o.codigo, o.nombre) " +
            " FROM Usuario o " +
            "WHERE o.codigo LIKE :codigo" + //CONCAT('%',:codigo,'%')
            "  AND o.codigoValorUsuario=:codigoValorUsuario " +
            "  AND o.codigoAmbiente=:codigoAmbiente " +
            "  AND o.fechaBaja IS NULL")
    List<Resumen> getListaUsuarioResumenPorPatron(@Param("codigo") String codigo,
                                                         @Param("codigoValorUsuario") String codigoValorUsuario,
                                                         @Param("codigoAmbiente") String codigoAmbiente);

    @Query("SELECT new bo.clync.pos.dao.Resumen(o.codigo, o.nombre) " +
            " FROM Usuario o " +
            "WHERE o.codigoValorUsuario=:codigoValorUsuario " +
            "  AND o.codigoAmbiente=:codigoAmbiente " +
            "  AND o.fechaBaja IS NULL")
    List<Resumen> getListaUsuarioResumen(@Param("codigoValorUsuario") String codigoValorUsuario,
                                                @Param("codigoAmbiente") String codigoAmbiente);

}
