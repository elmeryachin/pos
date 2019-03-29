package bo.clync.pos.servicios.transaccion.pedido;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseInit;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseList;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by eyave on 27-10-17.
 */
public interface PedidoServicio {

    TransaccionResponseInit init(String token);
    String getIdTransaccion( String nroMovimiento, String token );

    TransaccionResponse adicionar(String token, TransaccionRequest request, HttpServletRequest http) throws Exception;
    TransaccionResponse actualizar(String token, TransaccionRequest request, HttpServletRequest http) throws Exception;
    ServResponse eliminar(String token, String idTransaccion, HttpServletRequest http) throws Exception;
    TransaccionResponse obtener(String token, String id);
    TransaccionResponseList lista(String token);
}
