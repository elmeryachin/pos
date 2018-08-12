package bo.clync.pos.servicios.transaccion.transferencia;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseList;

public interface RecibirServicio {

    public TransaccionResponseList listaPorRecibir(String token);

    public TransaccionResponseList lista(String token);

    public TransaccionResponse obtenerPorRecibir(String token, String id);

    public TransaccionResponse obtener(String token, String id);

    ServResponse confirmarRecepcion(String token, String id) throws Exception ;

    ServResponse cancelarRecepcion(String token, String id) throws Exception ;



}
