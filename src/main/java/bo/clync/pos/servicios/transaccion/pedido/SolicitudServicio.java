package bo.clync.pos.servicios.transaccion.pedido;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseInit;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseList;

/**
 * Created by eyave on 27-10-17.
 */
public interface SolicitudServicio {

    TransaccionResponseList lista(String token);
    TransaccionResponseInit init(String token);
    TransaccionResponse adicionar(String token, TransaccionRequest request) throws Exception;
    TransaccionResponse actualizar(String token, TransaccionRequest request) throws Exception;
    ServResponse eliminar(String token, String idTransaccion) throws Exception;
    TransaccionResponse obtener(String token, String id);

    ServResponse confirmarLlegada(String token, String id) throws Exception ;



}
