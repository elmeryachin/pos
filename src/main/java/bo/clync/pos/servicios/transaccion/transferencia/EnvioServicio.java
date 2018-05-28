package bo.clync.pos.servicios.transaccion.transferencia;

import bo.clync.pos.dao.ServResponse;
import bo.clync.pos.dao.inventario.SucursalesResponseList;
import bo.clync.pos.dao.transaccion.generic.TransaccionRequest;
import bo.clync.pos.dao.transaccion.generic.TransaccionResponse;
import bo.clync.pos.dao.transaccion.generic.TransaccionResponseInit;
import bo.clync.pos.dao.articulo.ArticuloResponseList;
import bo.clync.pos.dao.articulo.ArticuloResponseMin;
import bo.clync.pos.dao.transaccion.generic.TransaccionResponseList;
import bo.clync.pos.dao.transaccion.transferencia.*;

public interface EnvioServicio {

    TransaccionResponseInit init(String token);

    TransaccionResponse nuevo(String token, TransaccionRequest request) throws Exception;
    TransaccionResponse actualizar(String token, TransaccionRequest request) throws Exception;
    ServResponse eliminar(String token, String idTransaccion) throws Exception;

    TransaccionResponseList lista(String token);
}
