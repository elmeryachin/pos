package bo.clync.pos.repository.common;

import bo.clync.pos.arquetipo.tablas.PosInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by eyave on 06-11-17.
 */
public interface PosInventarioRepository extends JpaRepository<PosInventario, String> {

    @Query("SELECT o " +
           "  FROM PosInventario o " +
           " WHERE o.codigoAmbiente = :codigoAmbiente " +
           "   AND o.codigoArticulo = :codigoArticulo " +
           "   AND o.fechaBaja IS NULL")
    PosInventario getInventario(@Param("codigoAmbiente") String codigoAmbiente,
                                       @Param("codigoArticulo") String codigoArticulo);


}
