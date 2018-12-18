package bo.clync.pos.servicios.transaccion.transferencia;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseList;
import bo.clync.pos.servicios.discos.DiscoServicio;
import bo.clync.pos.utilitarios.UtilsDisco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseInit;
import bo.clync.pos.servicios.transaccion.generic.TransaccionServicio;
import bo.clync.pos.utilitarios.UtilsDominio;

import javax.servlet.http.HttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class SolicitudManualServicioImpl implements SolicitudManualServicio {

    @Autowired
    private TransaccionServicio transaccionServicio;
    @Autowired
    private DiscoServicio discoServicio;

    @Override
    public TransaccionResponseInit init(String token) {
        return transaccionServicio.init(token, UtilsDominio.SOLICITUD_INTERNA);
    }
    
    @Override
    public TransaccionResponse adicionar(String token, TransaccionRequest request, HttpServletRequest http)  throws Exception {

       TransaccionResponse response = transaccionServicio.nuevo(token, request, UtilsDominio.SOLICITUD_INTERNA, UtilsDominio.SOLICITUD_INTERNA_PEDIDO, UtilsDominio.TIPO_PAGO_NO_REQUERIDO);

       if( response.isRespuesta() )
           this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, request, token));

       return response;
    }

    @Override
    public TransaccionResponse actualizar(String token, TransaccionRequest request, HttpServletRequest http) throws Exception {
        TransaccionResponse response = transaccionServicio.actualizar(token, request, UtilsDominio.SOLICITUD_INTERNA);

        if( response.isRespuesta() )
            this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, request, token));

        return response;
    }

    @Override
    public ServResponse eliminar(String token, String idTransaccion, HttpServletRequest http) throws Exception {
        ServResponse response = transaccionServicio.eliminar(token, idTransaccion, UtilsDominio.SOLICITUD_INTERNA_PEDIDO);

        if( response.isRespuesta() )
            this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, null, token));

        return response;
    }

    @Override
    public TransaccionResponseList lista(String token) {
        return transaccionServicio.lista(token, UtilsDominio.SOLICITUD_INTERNA, UtilsDominio.SOLICITUD_INTERNA_PEDIDO, UtilsDominio.TIPO_USUARIO_EMPLEADO);
    }

    @Override
    public TransaccionResponseList listaTodaSolicitud(String token) {
        return transaccionServicio.lista(token, UtilsDominio.SOLICITUD_INTERNA_AUX, UtilsDominio.SOLICITUD_INTERNA_PEDIDO, UtilsDominio.TIPO_USUARIO_EMPLEADO);
    }

    @Override
    public TransaccionResponseList listaConfirmados(String token) {
        return transaccionServicio.lista(token, UtilsDominio.SOLICITUD_INTERNA, UtilsDominio.SOLICITUD_INTERNA_CONFIRMADO, UtilsDominio.TIPO_USUARIO_EMPLEADO);
    }

    @Override
    public TransaccionResponse obtener(String token, String id) {
        return transaccionServicio.obtener(token, id, UtilsDominio.SOLICITUD_INTERNA, UtilsDominio.SOLICITUD_INTERNA_PEDIDO);
    }
}
