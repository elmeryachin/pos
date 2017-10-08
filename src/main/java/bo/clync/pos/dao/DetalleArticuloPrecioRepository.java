package bo.clync.pos.dao;

import bo.clync.pos.model.DetalleArticuloPrecio;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by eyave on 08-10-17.
 */
public interface DetalleArticuloPrecioRepository extends JpaRepository<DetalleArticuloPrecio, Integer> {


    DetalleArticuloPrecio findByCodigoArticulo(String codigo);

}
