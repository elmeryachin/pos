package bo.clync.pos.servicios.transaccion.generic;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseInit;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseList;

public interface TransaccionServicio {

    TransaccionResponseInit init(String token, String dominio);
    TransaccionResponse nuevo(String token, TransaccionRequest request, String dominio, String valor, String tipoPago) throws Exception;
    TransaccionResponse actualizar(String token, TransaccionRequest request, String dominio) throws Exception;
    ServResponse eliminar(String token, String idTransaccion, String valor) throws Exception;
    TransaccionResponseList lista(String token, String dominio, String valor, String tipoUsuario);
    TransaccionResponse obtener(String token, String id, String dominio, String valor);

}
