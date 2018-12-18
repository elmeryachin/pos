package bo.clync.pos.servicios.transaccion.pedido;

import bo.clync.pos.arquetipo.objetos.transaccion.generic.*;
import bo.clync.pos.arquetipo.tablas.DetalleTransaccion;
import bo.clync.pos.arquetipo.tablas.Inventario;
import bo.clync.pos.arquetipo.tablas.Transaccion;
import bo.clync.pos.arquetipo.objetos.ServResponse;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by eyave on 27-10-17.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SolicitudServicioImp implements SolicitudServicio {

    @Autowired
    private TransaccionRepository transaccionRepository;
    @Autowired
    private DetalleTransaccionRepository detalleTransaccionRepository;
    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;
    @Autowired
    private InventarioRepository inventarioRepository;
    @Autowired
    private TransaccionServicio transaccionServicio;
    @Autowired
    private DiscoServicio discoServicio;
    @PersistenceContext
    private EntityManager em;

    @Override
    public TransaccionResponseInit init(String token) {
        return transaccionServicio.init(token, UtilsDominio.PEDIDO);
    }

    private java.math.BigInteger getSecuencialDetalleTransaccion() {
        return (java.math.BigInteger) em.createNativeQuery("SELECT nextval('detalle_transaccion_id_seq')").getSingleResult();
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
    public ServResponse confirmarLlegada(String token, String id, HttpServletRequest http) throws Exception {
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
                    if (transaccion.getCodigoValor().equals(UtilsDominio.PEDIDO_SOLICITUD)) {
                        if(transaccion.getIdUsuarioInicio().equals(idUsuario)) {
                            transaccion.setCodigoValor(UtilsDominio.PEDIDO_LLEGADA);
                            transaccion.setFechaFin(fecha);
                            transaccion.setFechaActualizacion(fecha);
                            transaccion.setOperadorActualizacion(String.valueOf(idUsuario));
                            transaccionRepository.save(transaccion);
                            List<DetalleTransaccion> detalles = detalleTransaccionRepository.findByIdTransaccionAndFechaBajaIsNull(id);
                            for (DetalleTransaccion dt : detalles) {
                                dt.setOperadorActualizacion(String.valueOf(idUsuario));
                                dt.setFechaActualizacion(fecha);
                                detalleTransaccionRepository.save(dt);
                                Inventario inventario = inventarioRepository.getInventario(transaccion.getCodigoAmbienteInicio(), dt.getCodigoArticulo());
                                if (inventario == null) {
                                    inventario = new Inventario();
                                    inventario.setCodigoAmbiente(transaccion.getCodigoAmbienteInicio());
                                    inventario.setCodigoArticulo(dt.getCodigoArticulo());
                                    inventario.setExistencia(dt.getCantidad());
                                    inventario.setFechaAlta(fecha);
                                    inventario.setOperadorAlta(String.valueOf(idUsuario));
                                } else {
                                    Integer cantidad = inventario.getExistencia() + dt.getCantidad();
                                    inventario.setExistencia(cantidad);
                                    inventario.setPorLlegar(inventario.getPorLlegar() - dt.getCantidad());
                                    inventario.setFechaActualizacion(fecha);
                                    inventario.setOperadorActualizacion(String.valueOf(idUsuario));
                                }
                                inventarioRepository.save(inventario);
                            }
                            this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, null, token));
                            response.setRespuesta(true);
                        } else {
                            response.setMensaje("Solo el usuario que creo la transaccion puede Confirmar la llegada");
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
    public TransaccionResponseList lista(String token) {
        return transaccionServicio.lista(token, UtilsDominio.PEDIDO, UtilsDominio.PEDIDO_SOLICITUD, UtilsDominio.TIPO_USUARIO_PROVEEDOR);
    }

}
