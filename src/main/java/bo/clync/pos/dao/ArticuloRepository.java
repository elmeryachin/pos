package bo.clync.pos.dao;

import bo.clync.pos.model.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/**
 * Created by eyave on 05-10-17.
 */
public interface ArticuloRepository extends JpaRepository<Articulo, String> {

    List<Articulo> findByCodigoIsLike(String codigo);

    List<Articulo> findByDescripcionIsLike(String descripcion);


}
