package bo.clync.pos.servicios.transaccion.llegada;


import bo.clync.pos.entity.DetalleTransaccion;
import bo.clync.pos.entity.Inventario;
import bo.clync.pos.entity.Transaccion;
import bo.clync.pos.model.MiExcepcion;
import bo.clync.pos.model.transaccion.llegada.*;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.common.CicloRepository;
import bo.clync.pos.repository.common.InventarioRepository;
import bo.clync.pos.repository.common.UsuarioRepository;
import bo.clync.pos.repository.transaccion.pedido.DetalleTransaccionRepository;
import bo.clync.pos.repository.transaccion.pedido.TransaccionRepository;
import bo.clync.pos.utilitarios.UtilsDominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by eyave on 02-11-17.
 */
@Service
@Transactional
public class LlegadaServicioImp implements LlegadaServicio {
    @Autowired
    private CicloRepository cicloRepository;
    @Autowired
    private TransaccionRepository transaccionRepository;
    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;
    @Autowired
    private DetalleTransaccionRepository detalleTransaccionRepository;
    @Autowired
    private InventarioRepository inventarioRepository;

    @Override
    public LlegadaResponse confirmarLlegada(String token, LlegadaRequest request) throws Exception{
        LlegadaResponse response = new LlegadaResponse();
        Transaccion transaccion = null;
        DetalleTransaccion detalle = null;
        Date fecha = null;
        try {
            fecha = new Date();
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if(arrayId != null) {
                Integer idUsuario = (Integer) arrayId[0];//String codigoAmbiente = (String) arrayId[1];
                transaccion = transaccionRepository.getTransaccion(request.getLlegadaObjeto().getId());
                if (transaccion != null) {
                    if (transaccion.getCodigoValor().equals(UtilsDominio.PEDIDO_SOLICITUD)) {
                        transaccion.setCodigoValor(UtilsDominio.PEDIDO_LLEGADA);
                        transaccion.setObservacion(request.getLlegadaObjeto().getObservacion());
                        transaccion.setFechaActualizacion(fecha);
                        transaccion.setOperadorActualizacion(String.valueOf(idUsuario));
                        transaccionRepository.save(transaccion);
                        request.getLlegadaObjeto().setId(transaccion.getId());
                        for (LlegadaDetalle llegada : request.getLlegadaObjeto().getLista()) {
                            if (llegada.getId() != null) {
                                detalle = detalleTransaccionRepository.getDetalleTransaccion(llegada.getId());
                                detalle.setCantidadOficial(llegada.getCantidad());
                                detalle.setPrecioOficial(llegada.getPrecio());
                                detalle.setObservacion(llegada.getObservacion());
                                detalle.setOperadorActualizacion(String.valueOf(idUsuario));
                                detalle.setFechaActualizacion(fecha);
                                detalleTransaccionRepository.save(detalle);
                                Inventario inventario = inventarioRepository.getInventario(transaccion.getCodigoAmbienteInicio(), detalle.getCodigoArticulo());
                                if(inventario == null) {
                                    inventario = new Inventario();
                                    inventario.setCodigoAmbiente(transaccion.getCodigoAmbienteInicio());
                                    inventario.setCodigoArticulo(detalle.getCodigoArticulo());
                                    inventario.setExistencia(detalle.getCantidadOficial());
                                    inventario.setFechaAlta(fecha);
                                    inventario.setOperadorAlta(String.valueOf(idUsuario));
                                } else {
                                    Integer cantidad = inventario.getExistencia() + detalle.getCantidadOficial();
                                    inventario.setExistencia(cantidad);
                                    inventario.setFechaActualizacion(fecha);
                                    inventario.setOperadorActualizacion(String.valueOf(idUsuario));
                                }
                                inventarioRepository.save(inventario);
                            }
                        }
                        response.setLlegadaObjeto(request.getLlegadaObjeto());
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
        } catch(Exception e) {
            e.printStackTrace();
            response.setLlegadaObjeto(null);
            response.setMensaje("Error al confirmar la llegada ");
        }
        if(!response.isRespuesta())
            throw new Exception(response.getMensaje());
        return response;
    }

    // FALTA SERVICIO DE ELIMINAR LLEGADA Y DE ESTA FORMA DEVOLVER LA OPERACION REALIZADA.

    @Override
    public LlegadaResponseList lista(String token) {
        LlegadaResponseList response = new LlegadaResponseList();
        try {
            String codigoAmbiente = credencialRepository.getCodigoAmbienteByToken(token);
            Integer idCiclo = cicloRepository.getIdCiclo();
            List<LlegadaObjeto> lista = transaccionRepository.listaSolicitudesLlegada(codigoAmbiente, UtilsDominio.PEDIDO, UtilsDominio.PEDIDO_SOLICITUD, idCiclo, UtilsDominio.TIPO_USUARIO_PROVEEDOR);
            for (int i = 0; i < lista.size(); i++) {
                List<LlegadaDetalle> detalle = detalleTransaccionRepository.listaDetalleLlegada(lista.get(i).getId());
                lista.get(i).setLista(detalle);
            }
            response.setList(lista);
            response.setRespuesta(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al generar la lista de Pedidos");
        }
        return response;
    }

    @Override
    public LlegadaResponse obtener(String token, String id) {
        LlegadaResponse response = new LlegadaResponse();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if(arrayId != null) {
                //Integer idUsuario = (Integer) arrayId[0];//String codigoAmbiente = (String) arrayId[1];
                LlegadaObjeto llegadaObjeto = transaccionRepository.getLlegadaObjeto(id);
                if(llegadaObjeto != null) {
                    llegadaObjeto.setLista(detalleTransaccionRepository.listaDetalleLlegada(id));
                    response.setLlegadaObjeto(llegadaObjeto);
                    response.setRespuesta(true);
                } else {
                    response.setMensaje("No se encontrol el registro");
                }
            } else {
                response.setMensaje("Las credenciales estan vencidas, ingrese desde el login nuevamente");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al obtener el pedido");
        }
        return response;
    }
}
