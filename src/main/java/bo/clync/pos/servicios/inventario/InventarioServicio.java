package bo.clync.pos.servicios.inventario;

import bo.clync.pos.arquetipo.objetos.inventario.ExistenciaResponseList;
import bo.clync.pos.arquetipo.objetos.inventario.SucursalesResponseList;

/**
 * Created by eyave on 03-12-17.
 */
public interface InventarioServicio {

    ExistenciaResponseList existenciaArticulo(String token, String codigo);

    SucursalesResponseList listaSucursales(String token);

}
