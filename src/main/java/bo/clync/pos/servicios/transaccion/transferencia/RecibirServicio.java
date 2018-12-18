package bo.clync.pos.servicios.transaccion.transferencia;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseInit;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseList;
import bo.clync.pos.utilitarios.UtilsDominio;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RecibirServicio {
	
    TransaccionResponseList listaPorRecibir(String token);

    TransaccionResponseList lista(String token);

    TransaccionResponse obtenerPorRecibir(String token, String id);

    String getIdTransaccionPorRecibir( String nroMovimiento, String token );

    TransaccionResponse obtener(String token, String id);

    ServResponse confirmarRecepcion(String token, String id, TransaccionRequest request, HttpServletRequest http) throws Exception ;

    ServResponse cancelarRecepcion(String token, String id, HttpServletRequest http) throws Exception ;



}
