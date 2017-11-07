package bo.clync.pos.servicios.transaccion.pedido;

import bo.clync.pos.model.MiExcepcion;
import bo.clync.pos.model.ServResponse;
import bo.clync.pos.model.transaccion.pedido.*;

/**
 * Created by eyave on 27-10-17.
 */
public interface PedidoServicio {

    PedidoResponseInit init(String token);
    ProveedorResponseMin obtenerProveedor(String token, String codigo);
    ProveedorResponseList listaProveedor(String token, String patron);
    ArticuloResponseMin obtenerArticulo(String token, String codigo);
    ArticuloResponseList existenciaArticulo(String token, String codigo);
    ArticuloResponseList listaArticulo(String token, String patron);
    PedidoResponse nuevo(String token, PedidoRequest request) throws Exception;
    PedidoResponseList lista(String token);
    PedidoResponse actualizar(String token, PedidoRequest request) throws Exception;
    ServResponse eliminar(String token, String idTransaccion) throws Exception;
    PedidoResponse obtener(String token, String id);

}