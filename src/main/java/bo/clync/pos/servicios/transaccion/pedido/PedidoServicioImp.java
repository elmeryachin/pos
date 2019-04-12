package bo.clync.pos.servicios.transaccion.pedido;

import bo.clync.pos.arquetipo.objetos.transaccion.generic.*;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.servicios.discos.DiscoServicio;
import bo.clync.pos.servicios.transaccion.generic.TransaccionServicio;
import bo.clync.pos.utilitarios.UtilsDisco;
import bo.clync.pos.utilitarios.UtilsDominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by eyave on 27-10-17.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PedidoServicioImp implements PedidoServicio {

    @Autowired
    private TransaccionServicio transaccionServicio;
    @Autowired
    private DiscoServicio discoServicio;

    @Override
    public TransaccionResponseInit init(String token) {
        return transaccionServicio.init(token, UtilsDominio.PEDIDO);
    }

    @Override
    public String getIdTransaccion( String nroMovimiento, String token ) {
        return transaccionServicio.getIdTransaccion(UtilsDominio.PEDIDO, nroMovimiento, token);
    }

    @Override
    public TransaccionResponse adicionar(String token, TransaccionRequest request, HttpServletRequest http) throws Exception {

        TransaccionResponse response = transaccionServicio.nuevo(token, request, UtilsDominio.PEDIDO, UtilsDominio.PEDIDO_SOLICITUD, UtilsDominio.TIPO_PAGO_PAGADO);
        if(response.isRespuesta())
            this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, request, token));
        return response;
    }

    @Override
    public TransaccionResponse actualizar(String token, TransaccionRequest request, HttpServletRequest http) throws Exception {
        TransaccionResponse response = transaccionServicio.actualizar(token, request, UtilsDominio.PEDIDO);
        if(response.isRespuesta())
            this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, request, token));
        return response;
    }

    @Override
    public ServResponse eliminar(String token, String idTransaccion, HttpServletRequest http) throws Exception {
        ServResponse response = transaccionServicio.eliminar(token, idTransaccion, UtilsDominio.PEDIDO_SOLICITUD);
        if(response.isRespuesta())
            this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, null, token));
        return response;
    }

    @Override
    public TransaccionResponse obtener(String token, String id) {
        return transaccionServicio.obtener(token, id, UtilsDominio.PEDIDO, UtilsDominio.PEDIDO_SOLICITUD);
    }

    @Override
    public TransaccionResponseList lista(String token) {
        return transaccionServicio.lista(token, UtilsDominio.PEDIDO, UtilsDominio.PEDIDO_SOLICITUD, UtilsDominio.TIPO_USUARIO_PROVEEDOR);
    }

}
