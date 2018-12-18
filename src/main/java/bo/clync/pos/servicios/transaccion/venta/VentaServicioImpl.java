package bo.clync.pos.servicios.transaccion.venta;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.*;
import bo.clync.pos.arquetipo.tablas.Transaccion;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
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
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class VentaServicioImpl implements VentaServicio {

    @Autowired
    private TransaccionServicio transaccionServicio;
    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;
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
        //validarLISTA DETALLE DE ARTICULOS POR SUCURSAL
        //String[] codigoArticulo = arrayArticulos(request);

        TransaccionResponse response = transaccionServicio.nuevo(token, request, UtilsDominio.VENTA, UtilsDominio.VENTA_REALIZADA, UtilsDominio.TIPO_PAGO_NO_REQUERIDO);

        if( response.isRespuesta() )
            this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, request, token));

        return response;
    }


    public String[] arrayArticulos(TransaccionRequest request) {
        List<TransaccionDetalle> list = request.getTransaccionObjeto().getLista();
        String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i).getCodigoArticulo();
        }
        return array;
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
        Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
        if(arrayId!=null) {
            Long idUsuario = (Long) arrayId[0];
            Transaccion transaccion = transaccionRepository.getTransaccion(id);
            if (transaccion != null) {
                if (transaccion.getCodigoValor().equals(UtilsDominio.VENTA_REALIZADA)) {
                    transaccion.setCodigoValor(UtilsDominio.VENTA_CONFIRMADA);
                    transaccion.setFechaActualizacion(new Date());
                    transaccion.setOperadorActualizacion(String.valueOf(idUsuario));
                    this.transaccionRepository.save(transaccion);
                    this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, null, token));
                    response.setRespuesta(true);
                } else {
                    response.setMensaje("La transaccion se encuentra en estado de " + transaccion.getCodigoValor());
                }
            } else {
                response.setMensaje("No se encontro la transaccion");
            }
        } else {
            response.setMensaje("Las credenciales vencieron, inicie session nuevamente.");
        }
        return response;
    }

    @Override
    public TransaccionResponseList listaConfirmados(String token) {
        return transaccionServicio.lista(token, UtilsDominio.VENTA, UtilsDominio.VENTA_CONFIRMADA, UtilsDominio.TIPO_USUARIO_CLIENTE);
    }


}
