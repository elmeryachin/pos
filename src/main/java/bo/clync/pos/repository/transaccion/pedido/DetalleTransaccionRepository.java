package bo.clync.pos.repository.transaccion.pedido;

import bo.clync.pos.entity.DetalleTransaccion;
import bo.clync.pos.model.transaccion.pedido.PedidoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * Created by eyave on 27-10-17.
 */
public interface DetalleTransaccionRepository extends JpaRepository<DetalleTransaccion, String> {

    @Query("SELECT new bo.clync.pos.model.transaccion.pedido.PedidoDetalle(o.id, o.codigoArticulo, o.cantidad, o.precio, o.observacion) " +
            " FROM DetalleTransaccion o " +
            "WHERE o.idTransaccion=:idTransaccion " +
            "  AND o.fechaBaja IS NULL")
    public List<PedidoDetalle> listaDetalle(@Param("idTransaccion") String idTransaccion);


    List<DetalleTransaccion> findByIdNotInAndFechaBajaIsNull(Collection<String> ids);

    List<DetalleTransaccion> findByIdTransaccionAndFechaBajaIsNull(String id);

    @Query("SELECT o.id " +
            " FROM DetalleTransaccion o " +
            "WHERE o.idTransaccion = :idTransaccion " +
            "  AND o.codigoArticulo = :codigoArticulo " +
            "  AND o.fechaBaja is null")
    String existeArticuloEnTransaccion(@Param("idTransaccion") String idTransaccion,
                                       @Param("codigoArticulo") String codigoArticulo);

    @Query("SELECT o " +
           "  FROM DetalleTransaccion  o " +
           " WHERE o.id = :id " +
           "   AND o.fechaBaja IS NULL")
    public DetalleTransaccion getDetalleTransaccion(@Param("id") String id);
}
