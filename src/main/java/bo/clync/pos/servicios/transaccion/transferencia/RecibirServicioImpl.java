package bo.clync.pos.servicios.transaccion.transferencia;

import bo.clync.pos.arquetipo.dto.DatosUsuario;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponseList;
import bo.clync.pos.arquetipo.tablas.PosInventario;
import bo.clync.pos.arquetipo.tablas.PosTransaccion;
import bo.clync.pos.arquetipo.tablas.PosTransaccionDetalle;
import bo.clync.pos.repository.common.AdmCredencialRepository;
import bo.clync.pos.repository.common.PosInventarioRepository;
import bo.clync.pos.repository.transaccion.pedido.TransaccionDetalleRepository;
import bo.clync.pos.repository.transaccion.pedido.TransaccionRepository;
import bo.clync.pos.servicios.discos.DiscoServicio;
import bo.clync.pos.servicios.transaccion.generic.TransaccionServicio;
import bo.clync.pos.utilitarios.UtilsDisco;
import bo.clync.pos.utilitarios.UtilsDominio;
import bo.clync.pos.utilitarios.UtilsOperacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class RecibirServicioImpl implements RecibirServicio {

    @Autowired
    private TransaccionServicio transaccionServicio;
    @Autowired
    private PosInventarioRepository posInventarioRepository;
    @Autowired
    private AdmCredencialRepository credencialRepository;
    @Autowired
    private TransaccionRepository transaccionRepository;
    @Autowired
    private TransaccionDetalleRepository transaccionDetalleRepository;
    @Autowired
    private DiscoServicio discoServicio;

    @Override
    public TransaccionResponseList listaPorRecibir(String token) {
        return transaccionServicio.lista(token, UtilsDominio.TRANSFERENCIA, UtilsDominio.TRANSFERENCIA_ENVIO_DESTINO_AUX, UtilsDominio.TIPO_USUARIO_PROVEEDOR);
    }

    @Override
    public TransaccionResponseList lista(String token) {
        return transaccionServicio.lista(token, UtilsDominio.TRANSFERENCIA, UtilsDominio.TRANSFERENCIA_RECIBIR, UtilsDominio.TIPO_USUARIO_PROVEEDOR);
    }

    @Override
    public TransaccionResponse obtenerPorRecibir(String token, String id) {
        return transaccionServicio.obtener(token, id, UtilsDominio.TRANSFERENCIA, UtilsDominio.TRANSFERENCIA_ENVIO);
    }

    @Override
    public String getIdTransaccionPorRecibir( String nroMovimiento, String token ) {
        return transaccionServicio.getIdTransaccion(UtilsDominio.TRANSFERENCIA, nroMovimiento, token);
    }


    @Override
    public TransaccionResponse obtener(String token, String id) {
        return transaccionServicio.obtener(token, id, UtilsDominio.TRANSFERENCIA, UtilsDominio.TRANSFERENCIA_RECIBIR);
    }

    @Override
    public ServResponse confirmarRecepcion(String token, String id, TransaccionRequest request, HttpServletRequest http) throws Exception {
        ServResponse response = new ServResponse();
        PosTransaccion posTransaccion = null;
        Date fecha = null;
        String dominio = null;
        try {
            if(request == null || request.getTransaccionObjeto() == null || !request.getTransaccionObjeto().getId().equals(id)) {
        		dominio = UtilsDominio.TRANSFERENCIA_RECIBIR;
        	} else {
        		dominio = UtilsDominio.TRANSFERENCIA_RECIBIR_EDIT;
        	}
            fecha = new Date();

            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            posTransaccion = transaccionRepository.getTransaccion(id);
            if (posTransaccion != null) {
                if (posTransaccion.getCodigoValor().equals(UtilsDominio.TRANSFERENCIA_ENVIO)) {
                    if (datosUsuario.getCodigoAmbiente().equals(posTransaccion.getCodigoAmbienteFin())) {
                        posTransaccion.setCodigoValor(dominio);
                        posTransaccion.setFechaFin(fecha);
                        posTransaccion.setFechaActualizacion(fecha);
                        posTransaccion.setOperadorActualizacion(datosUsuario.getIdUsuario().toString());
                        //posTransaccion.setObservacion(request.getTransaccionObjeto().getObservacion());
                        if(dominio == UtilsDominio.TRANSFERENCIA_RECIBIR_EDIT) {
                            //Identificado directo para ver que la posTransaccion fue editada por una sucursal.
                            posTransaccion.setObservacion("<editado>" + posTransaccion.getObservacion());
                        }
                        transaccionRepository.save(posTransaccion);
                        List<PosTransaccionDetalle> detalles = transaccionDetalleRepository.findByIdTransaccionAndFechaBajaIsNull(id);
                        for (PosTransaccionDetalle dt : detalles) {

                            PosInventario posInventario = posInventarioRepository.getInventario(posTransaccion.getCodigoAmbienteFin(), dt.getCodigoArticulo());
                            Integer cantidad = posInventario.getExistencia() + dt.getCantidad();
                            posInventario.setExistencia(cantidad);
                            posInventario.setPorRecibir(UtilsOperacion.getNumeroNoNulo(posInventario.getPorRecibir()) - dt.getCantidad());
                            posInventario.setFechaActualizacion(fecha);
                            posInventario.setOperadorActualizacion(datosUsuario.getIdUsuario().toString());
                            posInventarioRepository.save(posInventario);

                            PosInventario posInventario2 = posInventarioRepository.getInventario(posTransaccion.getCodigoAmbienteInicio(), dt.getCodigoArticulo());
                            posInventario2.setPorEntregar(posInventario2.getPorEntregar() - dt.getCantidad());
                            posInventario2.setFechaActualizacion(fecha);
                            posInventario2.setOperadorActualizacion(datosUsuario.getIdUsuario().toString());
                            posInventarioRepository.save(posInventario2);
                        }
                            
                        // Cuando se edita la posTransaccion se adiciona este registro.
                        if(dominio.equals(UtilsDominio.TRANSFERENCIA_RECIBIR_EDIT)) {
                            String dominioNuevo = UtilsDominio.TRANSFERENCIA_NLL;
                            String valorNuevo = UtilsDominio.TRANSFERENCIA_NLL_RECIBIR_NO_LLEGO;
                            String tipoPagoNuevo = UtilsDominio.TIPO_PAGO_NO_REQUERIDO;

                            List<bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionDetalle> listNuevo = request.getTransaccionObjeto().getLista();
                            Map<String, Integer> mapDatosOrigen = new HashMap<>();
                            	
                            for(PosTransaccionDetalle dt : detalles) {
                                mapDatosOrigen.put(dt.getCodigoArticulo(), dt.getCantidad());
                            }
                            Integer cantidadDetalle = 0 ;
                            BigDecimal precioDetalle =  new BigDecimal("0.00");

                            Map<String, Integer> mapDatosEditador = new HashMap<>();
                            for( bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionDetalle dt : listNuevo ) {
                                mapDatosEditador.put(dt.getCodigoArticulo(), dt.getCantidad());
                            }
                            for(PosTransaccionDetalle dt : detalles) {
                                if( mapDatosEditador.get(dt.getCodigoArticulo()) == null ) {
                                    // El registro eliminado se adiciona con cantidad 0
                                    bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionDetalle regEliminado = new bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionDetalle();
                                    regEliminado.setCodigoArticulo(dt.getCodigoArticulo());
                                    regEliminado.setCantidad(0);
                                    regEliminado.setPrecio(dt.getPrecio());
                                    listNuevo.add(regEliminado);
                                }
                            }
                            for( bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionDetalle dt : listNuevo ) {
                                Integer cantidad = mapDatosOrigen.get(dt.getCodigoArticulo());
                                if( cantidad != null ) {
                                    dt.setCantidad( cantidad - dt.getCantidad() );
                                } else {
                                    dt.setCantidad( - dt.getCantidad() );
                                }
                                cantidadDetalle = cantidadDetalle + dt.getCantidad();
                                precioDetalle = precioDetalle.add( dt.getPrecio().multiply(new BigDecimal(dt.getCantidad())) );
                                System.out.println("###### " + dt.getCodigoArticulo() + " : Cantidad : " + dt.getCantidad());
                            }
                            request.getTransaccionObjeto().setPrecio(precioDetalle);
                            request.getTransaccionObjeto().setCantidad(cantidadDetalle);
                            //En codigo destino se ingresa el codigo del ambiente que creo la primera posTransaccion
                            request.getTransaccionObjeto().setCodigo(posTransaccion.getCodigoAmbienteInicio());
                            this.transaccionServicio.nuevo(token, request, dominioNuevo, valorNuevo, tipoPagoNuevo);
                        }
                        this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, request, token));
                        response.setRespuesta(true);
                    } else {
                        response.setMensaje("La posTransaccion solo lo puede recepcionar " + posTransaccion.getCodigoAmbienteFin());
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
    public ServResponse cancelarRecepcion(String token, String id, HttpServletRequest http) throws Exception {
        ServResponse response = new ServResponse();
        PosTransaccion posTransaccion = null;
        Date fecha = null;
        try {
            fecha = new Date();

            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            posTransaccion = transaccionRepository.getTransaccion(id);
            if (posTransaccion != null) {

                if (posTransaccion.getCodigoValor().equals(UtilsDominio.TRANSFERENCIA_RECIBIR_EDIT)
                        || posTransaccion.getCodigoValor().equals(UtilsDominio.TRANSFERENCIA_RECIBIR)) {
                    if (posTransaccion.getCodigoAmbienteFin().equals(datosUsuario.getCodigoAmbiente())) {
                        if(posTransaccion.getCodigoValor().equals(UtilsDominio.TRANSFERENCIA_RECIBIR_EDIT)) {
                            transaccionServicio.eliminar(token, id + "A", UtilsDominio.TRANSFERENCIA_NLL_RECIBIR_NO_LLEGO );
                        }

                        posTransaccion.setCodigoValor(UtilsDominio.TRANSFERENCIA_ENVIO);
                        posTransaccion.setFechaActualizacion(fecha);
                        posTransaccion.setOperadorActualizacion(datosUsuario.getIdUsuario().toString());
                        posTransaccion.setObservacion(posTransaccion.getObservacion().replaceAll("<editado>",""));
                        transaccionRepository.save(posTransaccion);
                        List<PosTransaccionDetalle> detalles = transaccionDetalleRepository.findByIdTransaccionAndFechaBajaIsNull(id);
                        StringBuilder msjProdNegativos = null;
                        for (PosTransaccionDetalle detalle : detalles) {
                            PosInventario posInventario = posInventarioRepository.getInventario(posTransaccion.getCodigoAmbienteFin(), detalle.getCodigoArticulo());
                            Integer cantidad = posInventario.getExistencia() - detalle.getCantidad();
                            if ( cantidad < 0 ) {
                                if( msjProdNegativos == null ) {
                                    msjProdNegativos = new StringBuilder("Con Cantidad negativa: ");
                                }
                                msjProdNegativos.append( detalle.getCodigoArticulo() ) .append( ", ");
                            }
                            /*if (cantidad < 0) {
                                   response.setMensaje("Error no existe suficiente posInventario para cancelar esta llegada");
                               }*/
                            posInventario.setExistencia(cantidad);
                            posInventario.setPorRecibir(UtilsOperacion.getNumeroNoNulo(posInventario.getPorRecibir()) + detalle.getCantidad());
                            posInventario.setFechaActualizacion(fecha);
                            posInventario.setOperadorActualizacion(datosUsuario.getIdUsuario().toString());
                            posInventarioRepository.save(posInventario);

                            PosInventario posInventario2 = posInventarioRepository.getInventario(posTransaccion.getCodigoAmbienteInicio(), detalle.getCodigoArticulo());
                            posInventario2.setPorEntregar(posInventario2.getPorEntregar() + detalle.getCantidad());
                            posInventario2.setFechaActualizacion(fecha);
                            posInventario2.setOperadorActualizacion(datosUsuario.getIdUsuario().toString());
                            posInventarioRepository.save(posInventario2);
                        }

                        if (response.getMensaje() == null) {
                            this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, null, token));
                            response.setRespuesta(true);
                            if (msjProdNegativos != null ) response.setMensaje(msjProdNegativos.toString());
                        }
                    } else {
                        response.setMensaje("La posTransaccion solo lo puede revertir " + posTransaccion.getCodigoAmbienteFin());
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

}
