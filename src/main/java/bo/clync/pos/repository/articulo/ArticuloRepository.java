package bo.clync.pos.repository.articulo;

import bo.clync.pos.entity.Articulo;
import bo.clync.pos.model.Resumen;
import bo.clync.pos.model.articulo.lista.ResumenArticulo;
import bo.clync.pos.model.articulo.obtener.ObjetoArticulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by eyave on 05-10-17.
 */
public interface ArticuloRepository extends JpaRepository<Articulo, String> {

    Articulo findByCodigoAndFechaBajaIsNull(String codigo);

    @Query("SELECT new bo.clync.pos.model.articulo.lista.ResumenArticulo(a) " +
            " FROM Articulo a " +
            "WHERE a.fechaBaja is null")
    List<ResumenArticulo> getListResumenArticulos();

    @Query("SELECT new bo.clync.pos.model.articulo.lista.ResumenArticulo(a) " +
            " FROM Articulo a " +
            "WHERE a.codigo like :patron " +
            "  AND a.fechaBaja IS NULL")
    List<ResumenArticulo> getListResumenArticulosPorPatron(@Param("patron") String patron);

    @Query("SELECT new bo.clync.pos.model.articulo.obtener.ObjetoArticulo(a) " +
            " FROM Articulo a " +
            "WHERE a.codigo=:codigo " +
            "  AND a.fechaBaja is null")
    ObjetoArticulo getObtenerArticuloPorCodigo(@Param("codigo") String codigo);

    @Query("SELECT new bo.clync.pos.model.Resumen(o.codigo, o.nombre) " +
            " FROM Articulo o " +
            "WHERE o.codigo LIKE :patron " + //CONCAT('%',:codigo,'%')
            "  AND o.fechaBaja IS NULL")
    List<Resumen> getListaArticuloResumenPorCodigo(@Param("patron") String patron);

    @Query("SELECT new bo.clync.pos.model.Resumen(a.codigo, a.nombre) " +
            " FROM Articulo a " +
            "WHERE a.fechaBaja is null")
    List<Resumen> getListaArticuloResumen();

    @Query("SELECT o.precioVenta " +
            " FROM Articulo o " +
            "WHERE o.codigo=:codigo " +
            "  AND o.fechaBaja is null")
    public BigDecimal getPrecioVentaPorCodigo(@Param("codigo") String codigo);
}
