package bo.clync.pos.servicios.transaccion.venta;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseInit;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseList;

public interface VentaServicio {
    TransaccionResponseInit init(String token);
    TransaccionResponse adicionar(String token, TransaccionRequest request) throws Exception;
    TransaccionResponse actualizar(String token, TransaccionRequest request) throws Exception;
    ServResponse eliminar(String token, String idTransaccion) throws Exception;
    TransaccionResponseList lista(String token);
    TransaccionResponse obtener(String token, String id);
}
