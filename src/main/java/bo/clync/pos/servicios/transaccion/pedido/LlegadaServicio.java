package bo.clync.pos.servicios.transaccion.pedido;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseList;

import javax.servlet.http.HttpServletRequest;

public interface LlegadaServicio {

    TransaccionResponseList lista(String token);
    ServResponse cancelarLlegada(String token, String id, HttpServletRequest http)  throws Exception ;
    TransaccionResponse obtener(String token, String id);
}
