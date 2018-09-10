package bo.clync.pos.servicios.transaccion.transferencia;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseInit;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseList;
import bo.clync.pos.utilitarios.UtilsDominio;

public interface RecibirServicio {
	
    TransaccionResponseList listaPorRecibir(String token);

    TransaccionResponseList lista(String token);

    TransaccionResponse obtenerPorRecibir(String token, String id);

    TransaccionResponse obtener(String token, String id);

    ServResponse confirmarRecepcion(String token, String id, TransaccionRequest request) throws Exception ;

    ServResponse cancelarRecepcion(String token, String id) throws Exception ;



}
