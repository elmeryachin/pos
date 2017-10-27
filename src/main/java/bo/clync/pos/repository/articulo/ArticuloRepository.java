package bo.clync.pos.repository.articulo;

import bo.clync.pos.entity.Articulo;
import bo.clync.pos.model.articulo.lista.ResumenArticulo;
import bo.clync.pos.model.articulo.obtener.ObjetoArticulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by eyave on 05-10-17.
 */
public interface ArticuloRepository extends JpaRepository<Articulo, Integer> {

    Articulo findByCodigoAndFechaBajaIsNull(String codigo);

    Articulo findByCodigo(String codigo);

    List<Articulo> findByCodigoIsLike(String codigo);

    List<Articulo> findByDescripcionIsLike(String descripcion);

    @Query("SELECT new bo.clync.pos.model.articulo.lista.ResumenArticulo(a) FROM Articulo a where a.fechaBaja is null")
    List<ResumenArticulo> getListResumenArticulos();

    @Query("SELECT new bo.clync.pos.model.articulo.obtener.ObjetoArticulo(a) FROM Articulo a where a.codigo=:codigo and a.fechaBaja is null")
    ObjetoArticulo getObtenerArticuloPorCodigo(@Param("codigo") String codigo);
}
