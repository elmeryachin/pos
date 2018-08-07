package bo.clync.pos.servicios.transaccion.transferencia;

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
import bo.clync.pos.servicios.transaccion.generic.TransaccionServicio;
import bo.clync.pos.utilitarios.UtilsDominio;
import bo.clync.pos.utilitarios.UtilsOperacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class RecibirServicioImpl implements RecibirServicio {

    @Autowired
    private TransaccionServicio transaccionServicio;
    @Autowired
    private InventarioRepository inventarioRepository;
    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;
    @Autowired
    private TransaccionRepository transaccionRepository;
    @Autowired
    private DetalleTransaccionRepository detalleTransaccionRepository;

    @Override
    public TransaccionResponseList lista(String token) {
        return transaccionServicio.lista(token, UtilsDominio.TRANSFERENCIA, UtilsDominio.TRANSFERENCIA_RECIBIR, UtilsDominio.TIPO_USUARIO_PROVEEDOR);
    }

    @Override
    public TransaccionResponse obtener(String token, String id) {
        return transaccionServicio.obtener(token, id, UtilsDominio.TRANSFERENCIA, UtilsDominio.TRANSFERENCIA_RECIBIR);
    }

    @Override
    public ServResponse confirmarRecepcion(String token, String id) throws Exception {
        ServResponse response = new ServResponse();
        Transaccion transaccion = null;
        Date fecha = null;
        try {
            fecha = new Date();
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if (arrayId != null) {
                Integer idUsuario = (Integer) arrayId[0];
                String codigoAmbiente = (String) arrayId[1];
                System.out.println("idUsuario > " + idUsuario);
                System.out.println("codAmbient> " + codigoAmbiente);
                transaccion = transaccionRepository.getTransaccion(id);
                if (transaccion != null) {
                    if (transaccion.getCodigoValor().equals(UtilsDominio.TRANSFERENCIA_ENVIO)) {
                        if (codigoAmbiente.equals(transaccion.getCodigoAmbienteFin())) {
                            transaccion.setCodigoValor(UtilsDominio.TRANSFERENCIA_RECIBIR);
                            transaccion.setFechaFin(fecha);
                            transaccion.setFechaActualizacion(fecha);
                            transaccion.setOperadorActualizacion(String.valueOf(idUsuario));
                            transaccionRepository.save(transaccion);
                            List<DetalleTransaccion> detalles = detalleTransaccionRepository.findByIdTransaccionAndFechaBajaIsNull(id);
                            for (DetalleTransaccion dt : detalles) {
                                dt.setOperadorActualizacion(String.valueOf(idUsuario));
                                dt.setFechaActualizacion(fecha);
                                detalleTransaccionRepository.save(dt);
                                Inventario inventario = inventarioRepository.getInventario(transaccion.getCodigoAmbienteFin(), dt.getCodigoArticulo());
                                Integer cantidad = inventario.getExistencia() + dt.getCantidad();
                                inventario.setExistencia(cantidad);
                                inventario.setPorRecibir(UtilsOperacion.getNumeroNoNulo(inventario.getPorRecibir()) - dt.getCantidad());
                                inventario.setFechaActualizacion(fecha);
                                inventario.setOperadorActualizacion(String.valueOf(idUsuario));
                                inventarioRepository.save(inventario);
                            }
                            response.setRespuesta(true);
                        } else {
                            response.setMensaje("La transaccion solo lo puede recepcionar " + transaccion.getCodigoAmbienteFin());
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
    public ServResponse cancelarRecepcion(String token, String id) throws Exception {
        ServResponse response = new ServResponse();
        Transaccion transaccion = null;
        Date fecha = null;
        try {
            fecha = new Date();
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if (arrayId != null) {
                Integer idUsuario = (Integer) arrayId[0];
                String codigoAmbiente = (String) arrayId[1];
                transaccion = transaccionRepository.getTransaccion(id);
                if (transaccion != null) {

                    if (transaccion.getCodigoValor().equals(UtilsDominio.TRANSFERENCIA_RECIBIR)) {
                        if (transaccion.getCodigoAmbienteFin().equals(codigoAmbiente)) {
                            transaccion.setCodigoValor(UtilsDominio.TRANSFERENCIA_ENVIO);
                            transaccion.setFechaActualizacion(fecha);
                            transaccion.setOperadorActualizacion(String.valueOf(idUsuario));
                            transaccionRepository.save(transaccion);
                            List<DetalleTransaccion> detalles = detalleTransaccionRepository.findByIdTransaccionAndFechaBajaIsNull(id);
                            for (DetalleTransaccion detalle : detalles) {
                                Inventario inventario = inventarioRepository.getInventario(transaccion.getCodigoAmbienteFin(), detalle.getCodigoArticulo());
                                Integer cantidad = inventario.getExistencia() - detalle.getCantidad();
                                if (cantidad < 0) {
                                    response.setMensaje("Error no existe suficiente inventario para cancelar esta llegada");
                                }
                                inventario.setExistencia(cantidad);
                                inventario.setPorRecibir(UtilsOperacion.getNumeroNoNulo(inventario.getPorRecibir()) + detalle.getCantidad());
                                inventario.setFechaActualizacion(fecha);
                                inventario.setOperadorActualizacion(String.valueOf(idUsuario));
                                inventarioRepository.save(inventario);
                            }
                            if (response.getMensaje() == null) {
                                response.setRespuesta(true);
                            }
                        } else {
                            response.setMensaje("La transaccion solo lo puede revertir " + transaccion.getCodigoAmbienteFin());
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

}
