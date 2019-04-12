package bo.clync.pos.repository.transaccion.pedido;

import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionObjeto;
import bo.clync.pos.arquetipo.tablas.PosTransaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by eyave on 27-10-17.
 */
public interface TransaccionRepository extends JpaRepository<PosTransaccion, String> {

    @Query("SELECT max(o.nroMovimiento)" +
            " FROM PosTransaccion o " +
            "WHERE o.idUsuarioInicio=:idUsuario " +
            "  AND o.codigoAmbienteInicio=:codigoAmbienteInicio" +
            "  AND o.codigoDominio=:dominio " +
            "  AND o.ciclo=:ciclo") // + "  AND o.fechaBaja IS NULL")
    public Integer initMovimiento(@Param("idUsuario") Long idUsuario,
                                  @Param("codigoAmbienteInicio") String codigoAmbienteInicio,
                                  @Param("dominio") String dominio,
                                  @Param("ciclo") String ciclo);


    @Query("SELECT new bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionObjeto(o.id, o.fechaInicio, o.nroMovimiento, u.codigo, o.observacion, o.cantidad, o.precio) " +
            " FROM PosTransaccion o, Usuario u " +
            "WHERE o.codigoAmbienteInicio=:codigoAmbienteInicio" +
            "  AND o.codigoDominio=:dominio " +
            "  AND o.codigoValor=:codigoValor " +
            "  AND o.ciclo=:ciclo " +
            "  AND o.idUsuarioFin=u.id " +
            "  AND u.codigoValorUsuario=:codigoValorUsuario " +
            "  AND u.codigoAmbiente=:codigoAmbienteInicio " +
            "  AND o.fechaBaja IS NULL" +
            " ORDER BY o.fechaInicio DESC, o.nroMovimiento DESC")
    List<TransaccionObjeto> listaPedidos(@Param("codigoAmbienteInicio") String codigoAmbienteInicio,
                                         @Param("dominio") String dominio,
                                         @Param("codigoValor") String codigoValor,
                                         @Param("ciclo") String ciclo,
                                         @Param("codigoValorUsuario") String codigoValorUsuario);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionObjeto(o.id, o.fechaInicio, o.nroMovimiento, o.codigoAmbienteFin, o.observacion, o.cantidad, o.precio) " +
            " FROM PosTransaccion o " +
            "WHERE o.codigoAmbienteInicio=:codigoAmbienteInicio" +
            "  AND o.codigoDominio=:dominio " +
            "  AND o.codigoValor=:codigoValor " +
            "  AND o.ciclo=:ciclo " +
            "  AND o.fechaBaja IS NULL" +
            " ORDER BY o.fechaInicio DESC, o.nroMovimiento DESC")
    List<TransaccionObjeto> listaTransferenciasEnviosPorOrigen(@Param("codigoAmbienteInicio") String codigoAmbienteInicio,
                                                               @Param("dominio") String dominio,
                                                               @Param("codigoValor") String codigoValor,
                                                               @Param("ciclo") String ciclo);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionObjeto(o.id, o.fechaInicio, o.nroMovimiento, o.codigoAmbienteFin, o.observacion, o.cantidad, o.precio) " +
            " FROM PosTransaccion o " +
            "WHERE o.codigoDominio=:dominio " +
            "  AND o.codigoValor=:codigoValor " +
            "  AND o.ciclo=:ciclo " +
            "  AND o.fechaBaja IS NULL" +
            " ORDER BY o.fechaInicio DESC, o.nroMovimiento DESC")
    List<TransaccionObjeto> listaTransferenciasEnviosTodos(@Param("dominio") String dominio,
                                                           @Param("codigoValor") String codigoValor,
                                                           @Param("ciclo") String ciclo);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionObjeto(o.id, o.fechaInicio, o.nroMovimiento, o.codigoAmbienteFin, o.observacion, o.cantidad, o.precio) " +
            " FROM PosTransaccion o " +
            "WHERE o.codigoAmbienteFin=:codigoAmbienteFin" +
            "  AND o.codigoDominio=:dominio " +
            "  AND o.codigoValor=:codigoValor " +
            "  AND o.ciclo=:ciclo " +
            "  AND o.fechaBaja IS NULL" +
            " ORDER BY o.fechaInicio DESC, o.nroMovimiento DESC")
    List<TransaccionObjeto> listaTransferenciasEnviosPorDestino(@Param("codigoAmbienteFin") String codigoAmbienteFin,
                                                                @Param("dominio") String dominio,
                                                                @Param("codigoValor") String codigoValor,
                                                                @Param("ciclo") String ciclo);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionObjeto(o.id, o.fechaInicio, o.nroMovimiento, o.codigoAmbienteFin, o.observacion, o.cantidad, o.precio) " +
            " FROM PosTransaccion o " +
            "WHERE o.codigoAmbienteFin=:codigoAmbienteFin" +
            "  AND o.codigoDominio=:dominio " +
            "  AND (o.codigoValor=:codigoValor " +
            "   OR o.codigoValor=:codigoValor2) " +
            "  AND o.ciclo=:ciclo " +
            "  AND o.fechaBaja IS NULL" +
            " ORDER BY o.fechaInicio DESC, o.nroMovimiento DESC")
    List<TransaccionObjeto> listaTransferenciasRecibidos(@Param("codigoAmbienteFin") String codigoAmbienteFin,
                                                         @Param("dominio") String dominio,
                                                         @Param("codigoValor") String codigoValor,
                                                         @Param("codigoValor2") String codigoValor2,
                                                         @Param("ciclo") String ciclo);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionObjeto(o.id, o.fechaInicio, o.nroMovimiento, o.codigoAmbienteFin, o.observacion, o.cantidad, o.precio) " +
            " FROM PosTransaccion o " +
            "WHERE o.codigoAmbienteInicio=:codigoAmbienteInicio" +
            "  AND o.codigoDominio=:dominio " +
            "  AND (o.codigoValor=:codigoValor " +
            "   OR o.codigoValor=:codigoValor2) " +
            "  AND o.ciclo=:ciclo " +
            "  AND o.fechaBaja IS NULL" +
            " ORDER BY o.fechaInicio DESC, o.nroMovimiento DESC")
    List<TransaccionObjeto> listaTransferenciasRecibidosPorOrigen(@Param("codigoAmbienteInicio") String codigoAmbienteInicio,
                                                                  @Param("dominio") String dominio,
                                                                  @Param("codigoValor") String codigoValor,
                                                                  @Param("codigoValor2") String codigoValor2,
                                                                  @Param("ciclo") String ciclo);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionObjeto(o.id, o.fechaInicio, o.nroMovimiento, u.codigo, o.observacion, o.cantidad, o.precio) " +
            " FROM PosTransaccion o, Usuario u " +
            "WHERE o.codigoAmbienteInicio=:codigoAmbienteInicio" +
            "  AND o.codigoDominio=:dominio " +
            "  AND o.codigoValor=:codigoValor " +
            "  AND o.ciclo=:ciclo " +
            "  AND o.idUsuarioFin=u.id " +
            "  AND u.codigoValorUsuario=:codigoValorUsuario " +//"  AND u.codigoAmbiente=:codigoAmbienteInicio " +
            "  AND o.fechaBaja IS NULL" +
            " ORDER BY o.fechaInicio DESC, o.nroMovimiento DESC")
    List<TransaccionObjeto> listaVentas(@Param("codigoAmbienteInicio") String codigoAmbienteInicio,
                                        @Param("dominio") String dominio,
                                        @Param("codigoValor") String codigoValor,
                                        @Param("ciclo") String ciclo,
                                        @Param("codigoValorUsuario") String codigoValorUsuario);

    @Query("SELECT o " +
            "  FROM PosTransaccion o " +
            " WHERE o.id = :id " +
            "   AND o.fechaBaja IS NULL")
    PosTransaccion getTransaccion(@Param("id") String id);

    PosTransaccion findByIdAndFechaBajaIsNull(String id);

    @Query("SELECT o.nroMovimiento, o.fechaAlta " +
            " FROM PosTransaccion o " +
            "WHERE o.idUsuarioInicio=:idUsuario " +
            "  AND o.codigoAmbienteInicio=:codigoAmbienteInicio" +
            "  AND o.codigoDominio=:dominio " +
            "  AND o.ciclo=:ciclo " +
            "  AND o.nroMovimiento=:nroMovimiento") //"  AND_o.fechaBaja IS_NULL")
    public Object existeNroMovimiento(@Param("idUsuario") Long idUsuario,
                                      @Param("codigoAmbienteInicio") String codigoAmbienteInicio,
                                      @Param("dominio") String dominio,
                                      @Param("ciclo") String ciclo,
                                      @Param("nroMovimiento") Integer nroMovimiento);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionObjeto(o.id, o.fechaInicio, o.nroMovimiento, u.codigo, o.observacion, o.cantidad, o.precio ) " +
            "  FROM PosTransaccion o, Usuario u " +
            " WHERE o.id = :id " +
            "   AND o.idUsuarioFin=u.id " +
            "   AND o.codigoDominio = :dominio " +
            "   AND o.codigoValor = :valor " +
            "   AND o.fechaBaja IS NULL")
    public TransaccionObjeto getTransaccionObjeto(@Param("id") String id,
                                                  @Param("dominio") String dominio,
                                                  @Param("valor") String valor);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionObjeto(o.id, o.fechaInicio, o.nroMovimiento, a.codigo, o.observacion, o.cantidad, o.precio ) " +
            "  FROM PosTransaccion o, Ambiente a " +
            " WHERE o.id = :id " +
            "   AND o.codigoAmbienteFin=a.codigo " +
            "   AND o.codigoDominio = :dominio " +
            "   AND o.codigoValor = :valor " +
            "   AND o.fechaBaja IS NULL")
    public TransaccionObjeto getTransaccionObjetoAmbiente(@Param("id") String id,
                                                          @Param("dominio") String dominio,
                                                          @Param("valor") String valor);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionObjeto(o.id, o.fechaInicio, o.nroMovimiento, a.codigo, o.observacion, o.cantidad, o.precio ) " +
            "  FROM PosTransaccion o, Ambiente a " +
            " WHERE o.id = :id " +
            "   AND o.codigoAmbienteInicio=a.codigo " +
            "   AND o.codigoDominio = :dominio " +
            "   AND o.codigoValor = :valor " +
            "   AND o.fechaBaja IS NULL")
    public TransaccionObjeto getTransaccionObjetoAmbientePorOrigen(@Param("id") String id,
                                                          @Param("dominio") String dominio,
                                                          @Param("valor") String valor);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionObjeto(o.id, o.fechaInicio, o.nroMovimiento, a.codigo, o.observacion, o.cantidad, o.precio ) " +
            "  FROM PosTransaccion o, Ambiente a " +
            " WHERE o.id = :id " +
            "   AND o.codigoAmbienteInicio=a.codigo " +
            "   AND o.codigoDominio = :dominio " +
            "   AND o.codigoValor = :valor " +
            "   AND o.fechaBaja IS NULL")
    public TransaccionObjeto getTransaccionObjetoAmbientePorOrigenAll(@Param("id") String id,
                                                                   @Param("dominio") String dominio,
                                                                   @Param("valor") String valor);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionObjeto(o.id, o.fechaInicio, o.nroMovimiento, u.codigo, o.observacion, o.cantidad, o.precio ) " +
            "  FROM PosTransaccion o, Usuario u " +
            " WHERE o.id = :id " +
            "   AND o.idUsuarioFin=u.id " +
            "   AND o.codigoDominio = :dominio " +
            "   AND o.codigoValor = :valor ")
    public TransaccionObjeto getTransaccionObjetoAll(@Param("id") String id,
                                                     @Param("dominio") String dominio,
                                                     @Param("valor") String valor);


    @Query("SELECT new bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionObjeto(o.id, o.fechaInicio, o.nroMovimiento, a.codigo, o.observacion, o.cantidad, o.precio ) " +
            "  FROM PosTransaccion o, Ambiente a" +
            " WHERE o.id = :id " +
            "   AND o.codigoAmbienteFin=a.codigo " +
            "   AND o.codigoDominio = :dominio " +
            "   AND o.codigoValor = :valor ")
    public TransaccionObjeto getTransaccionObjetoAmbienteAll(@Param("id") String id,
                                                             @Param("dominio") String dominio,
                                                             @Param("valor") String valor);

}
