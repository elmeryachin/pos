package bo.clync.pos.servicios.transaccion.transferencia;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseInit;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseList;

import javax.servlet.http.HttpServletRequest;

public interface EnvioServicio {

    TransaccionResponseInit init(String token);

    TransaccionResponse adicionar(String token, TransaccionRequest request, HttpServletRequest http) throws Exception;
    TransaccionResponse actualizar(String token, TransaccionRequest request, HttpServletRequest http) throws Exception;
    ServResponse eliminar(String token, String idTransaccion, HttpServletRequest http) throws Exception;
    TransaccionResponseList lista(String token);
    TransaccionResponseList listaConfirmados(String token);
    ServResponse reconfirmar(String token, String id, HttpServletRequest http) throws Exception;
    TransaccionResponseList listaReConfirmados(String token);
    TransaccionResponse obtener(String token, String id);
    String getIdTransaccion( String nroMovimiento, String token );
    TransaccionResponse obtenerDiferencia(String token, String id);
}
