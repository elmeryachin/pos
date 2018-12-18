package bo.clync.pos.servicios.transaccion.pedido;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseList;
import bo.clync.pos.arquetipo.tablas.DetalleTransaccion;
import bo.clync.pos.arquetipo.tablas.Inventario;
import bo.clync.pos.arquetipo.tablas.Transaccion;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.common.InventarioRepository;
import bo.clync.pos.repository.transaccion.pedido.DetalleTransaccionRepository;
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
public class LlegadaServicioImpl implements LlegadaServicio {

    @Autowired
    private TransaccionServicio transaccionServicio;
    @Autowired
    private TransaccionRepository transaccionRepository;
    @Autowired
    private DetalleTransaccionRepository detalleTransaccionRepository;
    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;
    @Autowired
    private InventarioRepository inventarioRepository;
    @Autowired
    private DiscoServicio discoServicio;
    @Override
    public TransaccionResponseList lista(String token) {
        return transaccionServicio.lista(token, UtilsDominio.PEDIDO, UtilsDominio.PEDIDO_LLEGADA, UtilsDominio.TIPO_USUARIO_PROVEEDOR);
    }

    @Override
    public ServResponse cancelarLlegada(String token, String id, HttpServletRequest http) throws Exception {
        ServResponse response = new ServResponse();
        Transaccion transaccion = null;
        Date fecha = null;
        try {
            fecha = new Date();
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if (arrayId != null) {
                Long idUsuario = (Long) arrayId[0];
                transaccion = transaccionRepository.getTransaccion(id);
                if (transaccion != null) {
                    if (transaccion.getCodigoValor().equals(UtilsDominio.PEDIDO_LLEGADA)) {
                        if(transaccion.getIdUsuarioInicio().equals(idUsuario)) {
                            transaccion.setCodigoValor(UtilsDominio.PEDIDO_SOLICITUD);
                            transaccion.setFechaActualizacion(fecha);
                            transaccion.setOperadorActualizacion(String.valueOf(idUsuario));
                            transaccionRepository.save(transaccion);
                            List<DetalleTransaccion> detalles = detalleTransaccionRepository.findByIdTransaccionAndFechaBajaIsNull(id);
                            for (DetalleTransaccion detalle : detalles) {
                                Inventario inventario = inventarioRepository.getInventario(transaccion.getCodigoAmbienteInicio(), detalle.getCodigoArticulo());
                                Integer cantidad = inventario.getExistencia() - detalle.getCantidad();
                                if (cantidad < 0) {
                                    response.setMensaje("Error no existe suficiente inventario para cancelar esta llegada");
                                }
                                inventario.setExistencia(cantidad);
                                inventario.setPorLlegar(detalle.getCantidad());
                                inventario.setFechaActualizacion(fecha);
                                inventario.setOperadorActualizacion(String.valueOf(idUsuario));
                                inventarioRepository.save(inventario);
                            }
                            if (response.getMensaje() == null) {
                                this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, null, token));
                                response.setRespuesta(true);
                            }
                        } else {
                            response.setMensaje("Solo el usuario que creo la transaccion puede cancelar la llegada");
                        }
                    } else {
                        response.setMensaje("La transaccion se encuentra en estado de " + transaccion.getCodigoValor());
                    }
                } else {
                    response.setMensaje("No se encontro la transaccion");
                }
            } else {
                response.setMensaje("Las credenciales vencieron, inicie session nuevamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al confirmar la llegada ");
        }
        if (!response.isRespuesta())
            throw new Exception(response.getMensaje());
        return response;
    }

    @Override
    public TransaccionResponse obtener(String token, String id) {
        return transaccionServicio.obtener(token, id, UtilsDominio.PEDIDO, UtilsDominio.PEDIDO_LLEGADA);
    }

}
