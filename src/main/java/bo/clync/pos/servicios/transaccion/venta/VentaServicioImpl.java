package bo.clync.pos.servicios.transaccion.venta;

import bo.clync.pos.arquetipo.dto.DatosUsuario;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.*;
import bo.clync.pos.arquetipo.tablas.PosTransaccion;
import bo.clync.pos.repository.common.AdmCredencialRepository;
import bo.clync.pos.repository.transaccion.pedido.TransaccionRepository;
import bo.clync.pos.servicios.discos.DiscoServicio;
import bo.clync.pos.servicios.transaccion.generic.TransaccionServicio;
import bo.clync.pos.utilitarios.UtilsDisco;
import bo.clync.pos.utilitarios.UtilsDominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@Transactional(rollbackFor = Exception.class)
public class VentaServicioImpl implements VentaServicio {

    @Autowired
    private TransaccionServicio transaccionServicio;
    @Autowired
    private AdmCredencialRepository credencialRepository;
    @Autowired
    private TransaccionRepository transaccionRepository;
    @Autowired
    private DiscoServicio discoServicio;

    @Override
    public TransaccionResponseInit init(String token) {
        return transaccionServicio.init(token, UtilsDominio.VENTA);
    }

    @Override
    public TransaccionResponse adicionar(String token, TransaccionRequest request, HttpServletRequest http) throws Exception {

        TransaccionResponse response = transaccionServicio.nuevo(token, request, UtilsDominio.VENTA, UtilsDominio.VENTA_REALIZADA, UtilsDominio.TIPO_PAGO_NO_REQUERIDO);

        if( response.isRespuesta() )
            this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, request, token));

        return response;
    }

    @Override
    public TransaccionResponse actualizar(String token, TransaccionRequest request, HttpServletRequest http) throws Exception {

        TransaccionResponse response = transaccionServicio.actualizar(token, request, UtilsDominio.VENTA);

        if( response.isRespuesta() )
            this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, request, token));

        return response;
    }

    @Override
    public ServResponse eliminar(String token, String idTransaccion, HttpServletRequest http) throws Exception {

        ServResponse response = transaccionServicio.eliminar(token, idTransaccion, UtilsDominio.VENTA_REALIZADA);

        if( response.isRespuesta() )
            this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, null, token));

        return response;
    }

    @Override
    public TransaccionResponseList lista(String token) {
        return transaccionServicio.lista(token, UtilsDominio.VENTA, UtilsDominio.VENTA_REALIZADA, UtilsDominio.TIPO_USUARIO_CLIENTE);
    }

    @Override
    public TransaccionResponse obtener(String token, String id) {
        return transaccionServicio.obtener(token, id, UtilsDominio.VENTA, UtilsDominio.VENTA_REALIZADA);
    }

    @Override
    public String getIdTransaccion( String nroMovimiento, String token ) {
        return transaccionServicio.getIdTransaccion(UtilsDominio.VENTA, nroMovimiento, token);
    }

    @Override
    public ServResponse confirmar(String token, String id, HttpServletRequest http) throws Exception{
        ServResponse response = new ServResponse();

        DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

        PosTransaccion posTransaccion = transaccionRepository.getTransaccion(id);
        if (posTransaccion != null) {
            if (posTransaccion.getCodigoValor().equals(UtilsDominio.VENTA_REALIZADA)) {
                posTransaccion.setCodigoValor(UtilsDominio.VENTA_CONFIRMADA);
                posTransaccion.setFechaActualizacion(new Date());
                posTransaccion.setOperadorActualizacion(datosUsuario.getIdUsuario().toString());
                this.transaccionRepository.save(posTransaccion);
                this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, null, token));
                response.setRespuesta(true);
            } else {
                response.setMensaje("La posTransaccion se encuentra en estado de " + posTransaccion.getCodigoValor());
            }
        } else {
            response.setMensaje("No se encontro la posTransaccion");
        }
        return response;
    }

    @Override
    public TransaccionResponseList listaConfirmados(String token) {
        return transaccionServicio.lista(token, UtilsDominio.VENTA, UtilsDominio.VENTA_CONFIRMADA, UtilsDominio.TIPO_USUARIO_CLIENTE);
    }


}
