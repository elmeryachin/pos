package bo.clync.pos.servicios.transaccion.generic;

import bo.clync.pos.dao.ServResponse;
import bo.clync.pos.dao.transaccion.generic.*;
import bo.clync.pos.entity.DetalleTransaccion;
import bo.clync.pos.entity.Transaccion;
import bo.clync.pos.repository.acceso.ConectadoRepository;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.articulo.ArticuloRepository;
import bo.clync.pos.repository.common.CicloRepository;
import bo.clync.pos.repository.common.UsuarioRepository;
import bo.clync.pos.repository.transaccion.pedido.DetalleTransaccionRepository;
import bo.clync.pos.repository.transaccion.pedido.TransaccionRepository;
import bo.clync.pos.utilitarios.UtilsDominio;
import bo.clync.pos.utilitarios.UtilsGeneral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class TransaccionServicioImpl implements TransaccionServicio {
    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;
    @Autowired
    private ConectadoRepository conectadoRepositor;
    @Autowired
    private TransaccionRepository transaccionRepository;
    @Autowired
    private CicloRepository cicloRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ArticuloRepository articuloRepository;
    @Autowired
    private DetalleTransaccionRepository detalleTransaccionRepository;
    @PersistenceContext
    private EntityManager em;

    private Object[] getConectados(String token) {
        Integer id = conectadoRepositor.obtenerIdUsuarioAmbienteCredencial(token);
        return (Object[]) credencialRepository.origenCredencial(id);
    }

    private java.math.BigInteger getSecuencialTransaccion() {
        return (java.math.BigInteger) em.createNativeQuery("SELECT nextval('transaccion_id_seq')").getSingleResult();
    }

    private java.math.BigInteger getSecuencialDetalleTransaccion() {
        return (java.math.BigInteger) em.createNativeQuery("SELECT nextval('detalle_transaccion_id_seq')").getSingleResult();
    }


    @Override
    public TransaccionResponseInit init(String token, String dominio) {
        TransaccionResponseInit init = new TransaccionResponseInit();
        try {
            Object[] o = getConectados(token);
            Integer nro = transaccionRepository.initMovimiento((Integer) o[0],  (String) o[1], dominio, cicloRepository.getIdCiclo());
            nro = nro == null ? 1 : nro + 1;
            init.setNroMovimiento(nro);
            init.setFechaMovimiento(UtilsGeneral.fechaActual());
            init.setRespuesta(true);
        } catch (Exception e) {
            init.setMensaje("Error al generar el nro de movimiento para " + dominio);
        }
        return init;
    }

    private void getTransaccion(String dominio, TransaccionRequest request, Transaccion transaccion, String codigoAmbiente) {
        if(dominio.equals(UtilsDominio.PEDIDO)) {
            transaccion.setIdUsuarioFin(usuarioRepository.getIdUsuarioProveedor(request.getTransaccionObjeto().getCodigo(), UtilsDominio.TIPO_USUARIO_PROVEEDOR, codigoAmbiente));
        } else if(dominio.equals(UtilsDominio.TRANSFERENCIA)) {
            transaccion.setCodigoAmbienteFin(request.getTransaccionObjeto().getCodigo());
        } else if(dominio.equals(UtilsDominio.VENTA)) {
            transaccion.setIdUsuarioFin(usuarioRepository.getIdUsuarioProveedor(request.getTransaccionObjeto().getCodigo(), UtilsDominio.TIPO_USUARIO_CLIENTE, codigoAmbiente));
        }
        transaccion.setObservacion(request.getTransaccionObjeto().getObservacion());
    }

    @Override
    public TransaccionResponse nuevo(String token, TransaccionRequest request, String dominio, String valor, String tipoPago) throws Exception {
        TransaccionResponse response = new TransaccionResponse();
        Transaccion transaccion = null;
        DetalleTransaccion detalle = null;
        Date fecha = new Date();
        try {
            if (request.getTransaccionObjeto() != null) {
                if (request.getTransaccionObjeto().getLista() != null && request.getTransaccionObjeto().getLista().size() > 0) {
                    Integer idCiclo = this.cicloRepository.getIdCiclo();
                    Object[] arrayId = (Object[]) this.credencialRepository.getIdUsuarioByToken(token);
                    Integer idUsuario = (Integer) arrayId[0];
                    String codigoAmbiente = (String) arrayId[1];
                    Object[] verificacion = (Object[]) this.transaccionRepository.existeNroMovimiento(idUsuario, codigoAmbiente, dominio, idCiclo, request.getTransaccionObjeto().getNroMovimiento());
                    if (verificacion == null || verificacion[0].equals(0l)) {
                        transaccion = new Transaccion();
                        String sid = idCiclo + "f" + codigoAmbiente + "f" + idUsuario + "f" + request.getTransaccionObjeto().getNroMovimiento();
                        transaccion.setId(sid);
                        transaccion.setIdCiclo(idCiclo);
                        transaccion.setCodigoDominio(dominio);
                        transaccion.setCodigoValor(valor);
                        transaccion.setCodigoValorPago(tipoPago);
                        transaccion.setNroMovimiento(request.getTransaccionObjeto().getNroMovimiento());
                        transaccion.setIdUsuarioInicio(idUsuario);
                        transaccion.setCodigoAmbienteInicio(codigoAmbiente);
                        transaccion.setFechaInicio(UtilsGeneral.convertirFecha(request.getTransaccionObjeto().getFechaMovimiento()));

                        this.getTransaccion(dominio, request, transaccion, codigoAmbiente);

                        transaccion.setFechaAlta(fecha);
                        transaccion.setOperadorAlta(String.valueOf(idUsuario));

                        this.transaccionRepository.save(transaccion);
                        request.getTransaccionObjeto().setId(transaccion.getId());

                        for (TransaccionDetalle pedido : request.getTransaccionObjeto().getLista()) {
                            detalle = new DetalleTransaccion();
                            String uid = transaccion.getId() + getSecuencialDetalleTransaccion().toString();
                            detalle.setId(uid);
                            detalle.setIdTransaccion(transaccion.getId());
                            detalle.setCodigoArticulo(pedido.getCodigoArticulo());
                            detalle.setCantidad(pedido.getCantidad());
                            detalle.setPrecio(pedido.getPrecio());
                            detalle.setPrecioSistema(this.articuloRepository.getPrecioVentaPorCodigo(pedido.getCodigoArticulo()));
                            detalle.setObservacion(pedido.getObservacion());
                            detalle.setOperadorAlta(String.valueOf(idUsuario));
                            detalle.setFechaAlta(fecha);
                            this.detalleTransaccionRepository.save(detalle);
                            pedido.setId(detalle.getId());
                        }
                        response.setTransaccionObjeto(request.getTransaccionObjeto());
                        response.setRespuesta(true);
                    } else {
                        response.setMensaje("Ya existe el nro movimiento " + verificacion[0] + ", se creo el " + verificacion[1]);
                    }
                } else {
                    response.setMensaje("Adicione al menos un articulo al detalle");
                }
            } else {
                response.setMensaje("El transaccion recibida se encuentra vacia.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setTransaccionObjeto(null);
            response.setMensaje("Error al guardar la transaccion ");
        }
        if (!response.isRespuesta())
            throw new Exception(response.getMensaje());
        return response;
    }

    @Override
    public TransaccionResponse actualizar(String token, TransaccionRequest request, String dominio) throws Exception {
        TransaccionResponse response = new TransaccionResponse();
        Transaccion transaccion = null;
        DetalleTransaccion detalle = null;
        Date fecha = new Date();
        try {
            Object[] arrayId = (Object[]) this.credencialRepository.getIdUsuarioByToken(token);
            Integer idUsuario = (Integer) arrayId[0];
            String codigoAmbiente = (String) arrayId[1];
            transaccion = this.transaccionRepository.findOne(request.getTransaccionObjeto().getId());
            if (transaccion != null) {

                this.getTransaccion(dominio, request, transaccion, codigoAmbiente);

                transaccion.setFechaActualizacion(fecha);
                transaccion.setOperadorActualizacion(String.valueOf(idUsuario));

                this.transaccionRepository.save(transaccion);
                request.getTransaccionObjeto().setId(transaccion.getId());

                List<String> listaActualizada = new ArrayList<>();
                for (TransaccionDetalle de : request.getTransaccionObjeto().getLista()) {
                    if (de.getId() != null) {
                        listaActualizada.add(de.getId());
                        detalle = this.detalleTransaccionRepository.findOne(de.getId());
                        detalle.setCodigoArticulo(de.getCodigoArticulo());
                        detalle.setCantidad(de.getCantidad());
                        detalle.setPrecio(de.getPrecio());
                        detalle.setObservacion(de.getObservacion());
                        detalle.setOperadorActualizacion(String.valueOf(idUsuario));
                        detalle.setFechaActualizacion(fecha);
                        this.detalleTransaccionRepository.save(detalle);
                    }
                }

                List<DetalleTransaccion> listBaja = this.detalleTransaccionRepository.findByIdNotInAndFechaBajaIsNull(listaActualizada);

                for (DetalleTransaccion de : listBaja) {
                    de.setFechaBaja(fecha);
                    de.setOperadorBaja(String.valueOf(idUsuario));
                    this.detalleTransaccionRepository.save(de);
                }

                for (TransaccionDetalle de : request.getTransaccionObjeto().getLista()) {
                    if (de.getId() == null) {
                        String idDetalleExistente = this.detalleTransaccionRepository.existeArticuloEnTransaccion(transaccion.getId(), de.getCodigoArticulo());
                        if (idDetalleExistente == null) {
                            detalle = new DetalleTransaccion();
                            detalle.setId(transaccion.getId() + this.getSecuencialDetalleTransaccion().toString());
                            detalle.setIdTransaccion(transaccion.getId());
                            detalle.setCodigoArticulo(de.getCodigoArticulo());
                            detalle.setCantidad(de.getCantidad());
                            detalle.setPrecio(de.getPrecio());
                            detalle.setPrecioSistema(this.articuloRepository.getPrecioVentaPorCodigo(de.getCodigoArticulo()));
                            detalle.setObservacion(de.getObservacion());
                            detalle.setOperadorAlta(String.valueOf(idUsuario));
                            detalle.setFechaAlta(fecha);
                            detalleTransaccionRepository.save(detalle);
                            de.setId(detalle.getId());
                            listaActualizada.add(de.getId());
                        } else {
                            response.setMensaje("Esta volviendo a insertar un articulo");
                        }
                    }
                }

                if (response.getMensaje() == null) {
                    response.setTransaccionObjeto(request.getTransaccionObjeto());
                    response.setRespuesta(true);
                }
            } else {
                response.setMensaje("No se encontro la transaccion");
            }
        } catch (Exception e) {
            response.setTransaccionObjeto(null);
            response.setMensaje("Error al actualizar la transaccion ");
        }
        if (!response.isRespuesta())
            throw new Exception(response.getMensaje());
        return response;
    }

    @Override
    public ServResponse eliminar(String token, String idTransaccion) throws Exception {
        ServResponse response = new ServResponse();
        try {
            Object[] arrayId = (Object[]) this.credencialRepository.getIdUsuarioByToken(token);
            if (arrayId != null) {
                Integer idUsuario = (Integer) arrayId[0];
                Date fecha = new Date();
                Transaccion transaccion = this.transaccionRepository.findOne(idTransaccion);
                transaccion.setFechaBaja(fecha);
                transaccion.setOperadorBaja(String.valueOf(idUsuario));
                this.transaccionRepository.save(transaccion);
                List<DetalleTransaccion> detalles = this.detalleTransaccionRepository.findByIdTransaccionAndFechaBajaIsNull(transaccion.getId());
                for (DetalleTransaccion detalle : detalles) {
                    detalle.setFechaBaja(fecha);
                    detalle.setOperadorBaja(String.valueOf(idUsuario));
                    this.detalleTransaccionRepository.save(detalle);
                }
                response.setRespuesta(true);
            } else {
                response.setMensaje("Las credenciales estan vencidas, ingrese desde el login nuevamente");
            }
        } catch (Exception e) {
            response.setMensaje("Error al dar de baja la transaccion");
        }
        if (!response.isRespuesta())
            throw new Exception(response.getMensaje());
        return response;
    }

    @Override
    public TransaccionResponseList lista(String token, String dominio, String valor, String tipoUsuario) {
        TransaccionResponseList response = new TransaccionResponseList();
        try {
            String codigoAmbiente = this.credencialRepository.getCodigoAmbienteByToken(token);
            Integer idCiclo = this.cicloRepository.getIdCiclo();
            List<TransaccionObjeto> lista = null;
            if(dominio.equals(UtilsDominio.PEDIDO)) {
                lista = this.transaccionRepository.listaPedidos(codigoAmbiente, dominio, valor, idCiclo, tipoUsuario);
            } else if(dominio.equals(UtilsDominio.TRANSFERENCIA)){
                lista = this.transaccionRepository.listaTransferencias(codigoAmbiente, dominio, valor, idCiclo);
            }
            for (TransaccionObjeto pedido : lista) {
                pedido.setLista(this.detalleTransaccionRepository.listaDetalle(pedido.getId()));
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
    public TransaccionResponse obtener(String token, String id) {
        TransaccionResponse response = new TransaccionResponse();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if (arrayId != null) {
                TransaccionObjeto tra = transaccionRepository.getPedidoObjeto(id);
                if (tra != null) {
                    tra.setLista(detalleTransaccionRepository.listaDetalle(id));
                    response.setTransaccionObjeto(tra);
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
