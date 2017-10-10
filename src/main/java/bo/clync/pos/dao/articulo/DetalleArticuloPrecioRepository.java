package bo.clync.pos.dao.articulo;

import bo.clync.pos.model.DetalleArticuloPrecio;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by eyave on 09-10-17.
 */
public interface DetalleArticuloPrecioRepository extends JpaRepository<DetalleArticuloPrecio, Integer> {

    DetalleArticuloPrecio findByIdArticuloAndFechaBajaIsNull(Integer idArticulo);

}
