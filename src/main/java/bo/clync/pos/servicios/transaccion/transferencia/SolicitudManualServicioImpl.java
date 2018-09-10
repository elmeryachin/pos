package bo.clync.pos.servicios.transaccion.transferencia;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseInit;
import bo.clync.pos.servicios.transaccion.generic.TransaccionServicio;
import bo.clync.pos.utilitarios.UtilsDominio;

@Service
@Transactional(rollbackFor = Exception.class)
public class SolicitudManualServicioImpl implements SolicitudManualServicio {

    @Autowired
    private TransaccionServicio transaccionServicio;

    @Override
    public TransaccionResponseInit init(String token) {
        return transaccionServicio.init(token, UtilsDominio.SOLICITUD_INTERNA);
    }
    
    @Override
    public TransaccionResponse adicionar(String token, TransaccionRequest request)  throws Exception {
       return transaccionServicio.nuevo(token, request, UtilsDominio.SOLICITUD_INTERNA, UtilsDominio.SOLICITUD_INTERNA_PEDIDO, UtilsDominio.TIPO_PAGO_NO_REQUERIDO);
    }

    @Override
    public TransaccionResponse actualizar(String token, TransaccionRequest request) throws Exception {
        return transaccionServicio.actualizar(token, request, UtilsDominio.SOLICITUD_INTERNA);
    }

    @Override
    public ServResponse eliminar(String token, String idTransaccion) throws Exception {
        return transaccionServicio.eliminar(token, idTransaccion, UtilsDominio.SOLICITUD_INTERNA_PEDIDO);
    }

    @Override
    public TransaccionResponseList lista(String token) {
        return transaccionServicio.lista(token, UtilsDominio.SOLICITUD_INTERNA, UtilsDominio.SOLICITUD_INTERNA_PEDIDO, UtilsDominio.TIPO_USUARIO_EMPLEADO);
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
