package bo.clync.pos.repository.transaccion.pedido;

import bo.clync.pos.arquetipo.tablas.PosTransaccionDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * Created by eyave on 27-10-17.
 */
public interface TransaccionDetalleRepository extends JpaRepository<PosTransaccionDetalle, String> {

    List<PosTransaccionDetalle> findByIdTransaccionAndIdNotInAndFechaBajaIsNull(String idTransaccion, Collection<String> ids);

    List<PosTransaccionDetalle> findByIdTransaccionAndFechaBajaIsNull(String id);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionDetalle(o.id, o.codigoArticulo, o.cantidad, o.precio, o.observacion) " +
            " FROM PosTransaccionDetalle o " +
            "WHERE o.idTransaccion=:idTransaccion " +
            "  AND o.fechaBaja IS NULL" +
            " ORDER BY o.id asc")
    List<bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionDetalle> listaDetalle(@Param("idTransaccion") String idTransaccion);

    @Query("SELECT o.id " +
            " FROM PosTransaccionDetalle o " +
            "WHERE o.idTransaccion = :idTransaccion " +
            "  AND o.codigoArticulo = :codigoArticulo " +
            "  AND o.fechaBaja is null")
    String existeArticuloEnTransaccion(@Param("idTransaccion") String idTransaccion,
                                       @Param("codigoArticulo") String codigoArticulo);

    @Query("SELECT o " +
           "  FROM PosTransaccionDetalle  o " +
           " WHERE o.id = :id " +
           "   AND o.fechaBaja IS NULL")
    public PosTransaccionDetalle getDetalleTransaccion(@Param("id") String id);

    @Query("SELECT count(o) " +
            "  FROM PosTransaccionDetalle  o " +
            " WHERE o.idTransaccion = :id ")
    public Long getDetalleTransaccionMaximoAll(@Param("id") String id);
}
