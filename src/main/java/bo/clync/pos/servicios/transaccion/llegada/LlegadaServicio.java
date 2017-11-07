package bo.clync.pos.servicios.transaccion.llegada;

import bo.clync.pos.model.MiExcepcion;
import bo.clync.pos.model.transaccion.llegada.LlegadaRequest;
import bo.clync.pos.model.transaccion.llegada.LlegadaResponse;
import bo.clync.pos.model.transaccion.llegada.LlegadaResponseList;
import bo.clync.pos.model.transaccion.pedido.PedidoResponseList;
import bo.clync.pos.model.transaccion.pedido.PedidoRequest;
import bo.clync.pos.model.transaccion.pedido.PedidoResponse;

/**
 * Created by eyave on 02-11-17.
 */
public interface LlegadaServicio {

    LlegadaResponse confirmarLlegada(String token, LlegadaRequest request) throws Exception;

    public LlegadaResponseList lista(String token);

    public LlegadaResponse obtener(String token, String id);
}
