package bo.clync.pos.servicios.transaccion.pedido;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseList;

public interface LlegadaServicio {

    TransaccionResponseList lista(String token);
    ServResponse cancelarLlegada(String token, String id)  throws Exception ;
    TransaccionResponse obtener(String token, String id);
}
