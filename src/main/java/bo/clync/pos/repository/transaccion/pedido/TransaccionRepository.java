package bo.clync.pos.repository.transaccion.pedido;

import bo.clync.pos.entity.Transaccion;
import bo.clync.pos.model.transaccion.pedido.PedidoObjeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by eyave on 27-10-17.
 */
public interface TransaccionRepository extends JpaRepository<Transaccion, String> {

    @Query("SELECT max(o.nroMovimiento)" +
            " FROM Transaccion o " +
            "WHERE o.idUsuarioInicio=:idUsuario " +
            "  AND o.codigoAmbienteInicio=:codigoAmbienteInicio" +
            "  AND o.codigoDominio=:dominio " +
            "  AND o.idCiclo=:idCiclo" +
            "  AND o.fechaBaja IS NULL")
    public Integer initMovimiento(@Param("idUsuario") Integer idUsuario,
                                  @Param("codigoAmbienteInicio") String codigoAmbienteInicio,
                                  @Param("dominio") String dominio,
                                  @Param("idCiclo") Integer idCiclo);


    @Query("SELECT new bo.clync.pos.model.transaccion.pedido.PedidoObjeto(o.id, o.fechaInicio, o.nroMovimiento, u.codigo, o.observacion) " +
            " FROM Transaccion o, Usuario u " +
            "WHERE o.codigoAmbienteInicio=:codigoAmbienteInicio" +
            "  AND o.codigoDominio=:dominio " +
            "  AND o.codigoValor=:codigoValor " +
            "  AND o.idCiclo=:idCiclo " +
            "  AND o.idUsuarioFin=u.id " +
            "  AND u.codigoValorUsuario=:codigoValorUsuario " +
            "  AND u.codigoAmbiente=:codigoAmbienteInicio " +
            "  AND o.fechaBaja IS NULL")
    public List<PedidoObjeto> listaTransacciones(@Param("codigoAmbienteInicio") String codigoAmbienteInicio,
                                                 @Param("dominio") String dominio,
                                                 @Param("codigoValor") String codigoValor,
                                                 @Param("idCiclo") Integer idCiclo,
                                                 @Param("codigoValorUsuario") String codigoValorUsuario);

    @Query("SELECT o " +
           "  FROM Transaccion o " +
           " WHERE o.id = :id " +
           "   AND o.fechaBaja IS NULL")
    public Transaccion getTransaccion(@Param("id") String id);
    public Transaccion findByIdAndFechaBajaIsNull(String id);
    @Query("SELECT o.nroMovimiento, o.fechaAlta " +
            " FROM Transaccion o " +
            "WHERE o.idUsuarioInicio=:idUsuario " +
            "  AND o.codigoAmbienteInicio=:codigoAmbienteInicio" +
            "  AND o.codigoDominio=:dominio " +
            "  AND o.idCiclo=:idCiclo " +
            "  AND o.nroMovimiento=:nroMovimiento" +
            "  AND o.fechaBaja IS NULL")
    public Object existeNroMovimiento(@Param("idUsuario") Integer idUsuario,
                                       @Param("codigoAmbienteInicio") String codigoAmbienteInicio,
                                       @Param("dominio") String dominio,
                                       @Param("idCiclo") Integer idCiclo,
                                       @Param("nroMovimiento") Integer nroMovimiento);

    @Query("SELECT new bo.clync.pos.model.transaccion.pedido.PedidoObjeto(o.id, o.fechaInicio, o.nroMovimiento, u.codigo, o.observacion ) " +
            "  FROM Transaccion o, Usuario u " +
            " WHERE o.id = :id " +
            "   AND o.idUsuarioFin=u.id " +
            "   AND o.fechaBaja IS NULL")
    public PedidoObjeto getPedidoObjeto(@Param("id") String id);
}
