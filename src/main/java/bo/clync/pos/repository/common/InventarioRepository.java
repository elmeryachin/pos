package bo.clync.pos.repository.common;

import bo.clync.pos.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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


    @Query("SELECT o " +
            "  FROM Inventario o " +
            " WHERE o.codigoArticulo = :codigoArticulo " +
            "   AND o.fechaBaja IS NULL")
    public List<Inventario> getInventario(@Param("codigoArticulo") String codigoArticulo);

    /*@Query("SELECT new bo.clync.pos.entity.Inventario(a.codigo, i.existencia, i.porLlegar, i.porEntregar, i.porRecibir) " +
           "  FROM Ambiente a LEFT OUTER JOIN Inventario i" +
           "    ON a.codigo = i.codigoAmbiente" +
           " WHERE a.fechaBaja IS NULL" +
           "   AND i.fechaBaja IS NULL")
    public List<Inventario> getInventarioAll(@Param("codigoArticulo") String codigoArticulo);*/
}
