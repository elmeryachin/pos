package bo.clync.pos.servicios.transaccion.venta;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseInit;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseList;

import javax.servlet.http.HttpServletRequest;

public interface VentaServicio {
    TransaccionResponseInit init(String token);
    TransaccionResponse adicionar(String token, TransaccionRequest request, HttpServletRequest http) throws Exception;
    TransaccionResponse actualizar(String token, TransaccionRequest request, HttpServletRequest http) throws Exception;
    ServResponse eliminar(String token, String idTransaccion, HttpServletRequest http) throws Exception;
    TransaccionResponseList lista(String token);
    TransaccionResponse obtener(String token, String id);
    String getIdTransaccion( String nroMovimiento, String token );

    ServResponse confirmar(String token, String id, HttpServletRequest http) throws Exception;
    TransaccionResponseList listaConfirmados(String token);

}
