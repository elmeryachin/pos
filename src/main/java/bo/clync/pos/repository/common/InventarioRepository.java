package bo.clync.pos.repository.common;

import bo.clync.pos.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by eyave on 06-11-17.
 */
public interface InventarioRepository  extends JpaRepository<Inventario, String> {

    @Query("SELECT o " +
           "  FROM Inventario o " +
           " WHERE o.codigoAmbiente = :codigoAmbiente " +
           "   AND o.codigoArticulo = :codigoArticulo " +
           "   AND o.fechaBaja IS NULL")
    public Inventario getInventario(@Param("codigoAmbiente") String codigoAmbiente,
                                    @Param("codigoArticulo") String codigoArticulo);

}
