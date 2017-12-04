package bo.clync.pos.servicios.inventario;

import bo.clync.pos.dao.inventario.ExistenciaResponseList;

/**
 * Created by eyave on 03-12-17.
 */
public interface InventarioServicio {

    ExistenciaResponseList existenciaArticulo(String token, String codigo);

}
