package bo.clync.pos.repository.common;

import bo.clync.pos.arquetipo.tablas.PosArticulo;
import bo.clync.pos.arquetipo.objetos.Resumen;
import bo.clync.pos.arquetipo.objetos.articulo.lista.ResumenArticulo;
import bo.clync.pos.arquetipo.objetos.articulo.obtener.ObjetoArticulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by eyave on 05-10-17.
 */
public interface PosArticuloRepository extends JpaRepository<PosArticulo, String> {

    PosArticulo findByCodigoAndFechaBajaIsNull(String codigo);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.articulo.lista.ResumenArticulo(a) " +
            " FROM PosArticulo a " +
            "WHERE a.fechaBaja is null")
    List<ResumenArticulo> getListResumenArticulos();

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.articulo.lista.ResumenArticulo(a) " +
            " FROM PosArticulo a " +
            "WHERE a.codigo like :patron " +
            "  AND a.fechaBaja IS NULL")
    List<ResumenArticulo> getListResumenArticulosPorPatron(@Param("patron") String patron);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.articulo.obtener.ObjetoArticulo(a) " +
            " FROM PosArticulo a " +
            "WHERE a.codigo=:codigo " +
            "  AND a.fechaBaja is null")
    ObjetoArticulo getObtenerArticuloPorCodigo(@Param("codigo") String codigo);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.Resumen(o.codigo, o.nombre) " +
            " FROM PosArticulo o " +
            "WHERE o.codigo LIKE :patron " + //CONCAT('%',:codigo,'%')
            "  AND o.fechaBaja IS NULL")
    List<Resumen> getListaArticuloResumenPorCodigo(@Param("patron") String patron);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.Resumen(a.codigo, a.nombre) " +
            " FROM PosArticulo a " +
            "WHERE a.fechaBaja is null")
    List<Resumen> getListaArticuloResumen();

    @Query("SELECT o.precioVenta " +
            " FROM PosArticulo o " +
            "WHERE o.codigo=:codigo " +
            "  AND o.fechaBaja is null")
    public BigDecimal getPrecioVentaPorCodigo(@Param("codigo") String codigo);

    @Query("SELECT count(o) " +
            " FROM PosTransaccionDetalle o " +
            "WHERE o.codigoArticulo=:codigo " +
            "  AND o.fechaBaja is null")
    public Integer getUsoArticulo(@Param("codigo") String codigo);

}
