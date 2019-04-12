package bo.clync.pos.servicios.transaccion.pedido;

import bo.clync.pos.arquetipo.dto.DatosUsuario;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseList;
import bo.clync.pos.arquetipo.tablas.PosInventario;
import bo.clync.pos.arquetipo.tablas.PosTransaccionDetalle;
import bo.clync.pos.arquetipo.tablas.PosTransaccion;
import bo.clync.pos.repository.common.AdmCredencialRepository;
import bo.clync.pos.repository.common.PosInventarioRepository;
import bo.clync.pos.repository.transaccion.pedido.TransaccionDetalleRepository;
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
    private TransaccionDetalleRepository transaccionDetalleRepository;
    @Autowired
    private AdmCredencialRepository credencialRepository;
    @Autowired
    private PosInventarioRepository posInventarioRepository;
    @Autowired
    private DiscoServicio discoServicio;

    private Date fechaActual = new Date();
    @Override
    public TransaccionResponseList lista(String token) {
        return transaccionServicio.lista(token, UtilsDominio.PEDIDO, UtilsDominio.PEDIDO_LLEGADA, UtilsDominio.TIPO_USUARIO_PROVEEDOR);
    }

    @Override
    public ServResponse confirmarLlegada(String token, String id, HttpServletRequest http) throws Exception {
        ServResponse response = new ServResponse();
        PosTransaccion posTransaccion = null;

        try {
            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            posTransaccion = transaccionRepository.getTransaccion(id);
            if (posTransaccion != null) {
                if (posTransaccion.getCodigoValor().equals(UtilsDominio.PEDIDO_SOLICITUD)) {
                    if(posTransaccion.getIdUsuarioInicio().equals(datosUsuario.getIdUsuario())) {

                        posTransaccion.setCodigoValor(UtilsDominio.PEDIDO_LLEGADA);
                        posTransaccion.setFechaFin(fechaActual);
                        posTransaccion.setFechaActualizacion(fechaActual);
                        posTransaccion.setOperadorActualizacion(datosUsuario.getIdUsuario().toString());
                        transaccionRepository.save(posTransaccion);

                        List<PosTransaccionDetalle> detalles = transaccionDetalleRepository.findByIdTransaccionAndFechaBajaIsNull(id);

                        for (PosTransaccionDetalle dt : detalles) {

                            dt.setOperadorActualizacion(datosUsuario.getIdUsuario().toString());
                            dt.setFechaActualizacion(fechaActual);
                            transaccionDetalleRepository.save(dt);
                            PosInventario posInventario = posInventarioRepository.getInventario(posTransaccion.getCodigoAmbienteInicio(), dt.getCodigoArticulo());
                            if (posInventario == null) {
                                posInventario = new PosInventario();
                                posInventario.setCodigoAmbiente(posTransaccion.getCodigoAmbienteInicio());
                                posInventario.setCodigoArticulo(dt.getCodigoArticulo());
                                posInventario.setExistencia(dt.getCantidad());
                                posInventario.setFechaAlta(fechaActual);
                                posInventario.setOperadorAlta(datosUsuario.getIdUsuario().toString());
                            } else {
                                Integer cantidad = posInventario.getExistencia() + dt.getCantidad();
                                posInventario.setExistencia(cantidad);
                                posInventario.setPorLlegar(posInventario.getPorLlegar() - dt.getCantidad());
                                posInventario.setFechaActualizacion(fechaActual);
                                posInventario.setOperadorActualizacion(datosUsuario.getIdUsuario().toString());
                            }
                            posInventarioRepository.save(posInventario);

                        }

                        this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, null, token));
                        response.setRespuesta(true);
                    } else {
                        response.setMensaje("Solo el usuario que creo la posTransaccion puede Confirmar la llegada");
                    }
                } else {
                    response.setMensaje("La posTransaccion se encuentra en estado de " + posTransaccion.getCodigoValor());
                }
            } else {
                response.setMensaje("No se encontro la posTransaccion");
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
    public ServResponse cancelarLlegada(String token, String id, HttpServletRequest http) throws Exception {
        ServResponse response = new ServResponse();
        PosTransaccion posTransaccion = null;

        try {

            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            posTransaccion = transaccionRepository.getTransaccion(id);
            if (posTransaccion.getCodigoValor().equals(UtilsDominio.PEDIDO_LLEGADA)) {

                if(posTransaccion.getIdUsuarioInicio().equals(datosUsuario.getIdUsuario())) {
                    posTransaccion.setCodigoValor(UtilsDominio.PEDIDO_SOLICITUD);
                    posTransaccion.setFechaActualizacion(fechaActual);
                    posTransaccion.setOperadorActualizacion(datosUsuario.getIdUsuario().toString());
                    transaccionRepository.save(posTransaccion);
                    List<PosTransaccionDetalle> detalles = transaccionDetalleRepository.findByIdTransaccionAndFechaBajaIsNull(id);
                    StringBuilder msjProdNegativos = null;

                    for (PosTransaccionDetalle detalle : detalles) {
                        PosInventario posInventario = posInventarioRepository.getInventario(posTransaccion.getCodigoAmbienteInicio(), detalle.getCodigoArticulo());
                        Integer cantidad = posInventario.getExistencia() - detalle.getCantidad();
                        if ( cantidad < 0 ) {
                            if( msjProdNegativos == null ) {
                                msjProdNegativos = new StringBuilder("Con Cantidad negativa: ");
                            }
                            msjProdNegativos.append( detalle.getCodigoArticulo() ) .append( ", ");
                        }
                        posInventario.setExistencia(cantidad);
                        posInventario.setPorLlegar(detalle.getCantidad());
                        posInventario.setFechaActualizacion(fechaActual);
                        posInventario.setOperadorActualizacion(datosUsuario.getIdUsuario().toString());
                        posInventarioRepository.save(posInventario);
                    }
                    this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, null, token));
                    response.setRespuesta(true);
                    if (msjProdNegativos != null ) response.setMensaje(msjProdNegativos.toString());
                } else {
                    response.setMensaje("Solo el usuario que creo la posTransaccion puede cancelar la llegada");
                }
            } else {
                response.setMensaje("La posTransaccion se encuentra en estado de " + posTransaccion.getCodigoValor());
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
