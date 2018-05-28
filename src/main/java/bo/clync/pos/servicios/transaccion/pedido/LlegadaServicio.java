package bo.clync.pos.servicios.transaccion.pedido;

import bo.clync.pos.dao.ServResponse;
import bo.clync.pos.dao.transaccion.generic.TransaccionResponseList;

public interface LlegadaServicio {

    TransaccionResponseList lista(String token);
    ServResponse cancelarLlegada(String token, String id)  throws Exception ;

}
