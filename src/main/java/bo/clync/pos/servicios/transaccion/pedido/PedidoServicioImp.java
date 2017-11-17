package bo.clync.pos.servicios.transaccion.pedido;

import bo.clync.pos.entity.DetalleTransaccion;
import bo.clync.pos.entity.Inventario;
import bo.clync.pos.entity.Transaccion;
import bo.clync.pos.entity.Usuario;
import bo.clync.pos.dao.Resumen;
import bo.clync.pos.dao.ServResponse;
import bo.clync.pos.dao.articulo.obtener.ObjetoArticulo;
import bo.clync.pos.dao.transaccion.pedido.*;
import bo.clync.pos.repository.articulo.ArticuloRepository;
import bo.clync.pos.repository.common.CicloRepository;
import bo.clync.pos.repository.acceso.ConectadoRepository;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.common.InventarioRepository;
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

/**
 * Created by eyave on 27-10-17.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PedidoServicioImp implements PedidoServicio {

    @Autowired
    private TransaccionRepository transaccionRepository;
    @Autowired
    private DetalleTransaccionRepository detalleTransaccionRepository;
    @Autowired
    private ConectadoRepository conectadoRepositor;
    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;
    @Autowired
    private CicloRepository cicloRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ArticuloRepository articuloRepository;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private InventarioRepository inventarioRepository;

    private Object[] getConectados(String token) {
        Integer id = conectadoRepositor.obtenerIdUsuarioAmbienteCredencial(token);
        return (Object[]) credencialRepository.origenCredencial(id);
    }

    @Override
    public PedidoResponseInit init(String token) {
        PedidoResponseInit init = new PedidoResponseInit();
        try {
            Object[] o = getConectados(token);
            Integer idUsuario = (Integer) o[0];
            String codigoAmbiente = (String) o[1];
            Integer idCiclo = cicloRepository.getIdCiclo();
            Integer nro = transaccionRepository.initMovimiento(idUsuario, codigoAmbiente, UtilsDominio.PEDIDO, idCiclo);
            nro = nro==null?1:nro+1;
            init.setNroMovimiento(nro);
            init.setFechaMovimiento(UtilsGeneral.fechaActual());
            init.setRespuesta(true);
        } catch (Exception e) {
            init.setMensaje("Error al generar el nro de movimiento");
        }
        return init;
    }

    @Override
    public ProveedorResponseMin obtenerProveedor(String token, String codigo) {
        Usuario usuario = null;
        ProveedorResponseMin response = new ProveedorResponseMin();
        String codigoAmbiente = null;
        try {
            codigoAmbiente = credencialRepository.getCodigoAmbienteByToken(token);
            usuario = usuarioRepository.findByCodigoAndCodigoValorUsuarioAndCodigoAmbienteAndFechaBajaIsNull(codigo, UtilsDominio.TIPO_USUARIO_PROVEEDOR, codigoAmbiente);
            if(usuario == null) {
                response.setMensaje("El proveedor no existe");
            } else {
                response.setCodigo(usuario.getCodigo());
                response.setNombre(usuario.getNombre());
                response.setRespuesta(true);
            }
        } catch (Exception e) {
            response.setMensaje("Error al recuperar el Proveedor");
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public ServResponse nuevoProveedor(String token, UsuarioRequest request) {
        ServResponse response = new ServResponse();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if(arrayId != null) {
                Integer idUsuario = (Integer) arrayId[0];
                String codigoAmbiente = (String) arrayId[1];
                Usuario usuario= usuarioRepository.findByCodigoAndCodigoValorUsuarioAndCodigoAmbienteAndFechaBajaIsNull(request.getCodigo(), UtilsDominio.TIPO_USUARIO_PROVEEDOR, codigoAmbiente);
                if(usuario == null) {
                    usuario = new Usuario();
                    usuario.setCodigo(request.getCodigo());
                    usuario.setNombre(request.getNombre());
                    usuario.setDireccion(request.getDireccion());
                    usuario.setTelefono(request.getTelefono());
                    usuario.setCodigoAmbiente(codigoAmbiente);
                    usuario.setCodigoValorUsuario(UtilsDominio.TIPO_USUARIO_PROVEEDOR);
                    usuario.setFechaAlta(new Date());
                    usuario.setOperadorAlta(String.valueOf(idUsuario));
                    usuarioRepository.save(usuario);
                    response.setRespuesta(true);
                } else {
                    response.setMensaje("El codigo ya existe");
                }
            } else {
                response.setMensaje("Las credenciales estan vencidas, ingrese desde el login nuevamente");
            }
        } catch(Exception e) {
            response.setMensaje("Error Al crear nuevo proveedork");
            e.printStackTrace();
        }
        return response;
    }
    @Override
    public ProveedorResponseList listaProveedor(String token, String patron) {
        ProveedorResponseList response = new ProveedorResponseList();
        try {
            String codigoAmbiente = credencialRepository.getCodigoAmbienteByToken(token);
            if(patron == null) response.setList(usuarioRepository.getListaUsuarioResumen(UtilsDominio.TIPO_USUARIO_PROVEEDOR, codigoAmbiente));
            else response.setList(usuarioRepository.getListaUsuarioResumenPorPatron(patron, UtilsDominio.TIPO_USUARIO_PROVEEDOR, codigoAmbiente));
            response.setRespuesta(true);
        } catch(Exception e) {
            response.setMensaje("Error al recuperar la lista de Proveedores");
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public ArticuloResponseMin obtenerArticulo(String token, String codigo) {
        ArticuloResponseMin response = new ArticuloResponseMin();
        try {
            ObjetoArticulo o = articuloRepository.getObtenerArticuloPorCodigo(codigo);
            if(o == null) {
                response.setMensaje("No se encontro el articulo con el codigo");
            } else {
                response.setCodigo(o.getCodigo());
                response.setNombre(o.getNombre());
                response.setPrecio(o.getPrecioVenta());
                response.setRespuesta(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error Al recuperar el articulo por codigo");
        }
        return response;
    }

    @Override
    public ArticuloResponseList existenciaArticulo(String token, String codigo) {
        ArticuloResponseList response = new ArticuloResponseList();
        response.setRespuesta(true);
        List<Resumen> lista = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Resumen resumen = new Resumen("T"+i, ""+(100+i));
            lista.add(resumen);
        }
        response.setList(lista);
        return response;
    }

    @Override
    public ArticuloResponseList listaArticulo(String token, String patron) {
        ArticuloResponseList response = new ArticuloResponseList();
        try {
            if(patron == null) response.setList(articuloRepository.getListaArticuloResumen());
            else response.setList(articuloRepository.getListaArticuloResumenPorCodigo(patron));
            response.setRespuesta(true);
        } catch (Exception e) {
            response.setMensaje("Error al listar los Articulos");
            e.printStackTrace();
        }
        return response;
    }
    private java.math.BigInteger getSecuencialTransaccion(){
        return (java.math.BigInteger) em.createNativeQuery("SELECT nextval('transaccion_id_seq')").getSingleResult();
    }

    private java.math.BigInteger getSecuencialDetalleTransaccion(){
        return (java.math.BigInteger) em.createNativeQuery("SELECT nextval('detalle_transaccion_id_seq')").getSingleResult();
    }
    @Override
    public PedidoResponse nuevo(String token, PedidoRequest request) throws Exception{
        PedidoResponse response = new PedidoResponse();
        Transaccion transaccion = null;
        DetalleTransaccion detalle = null;
        Date fecha = new Date();
        try {
            Integer idCiclo = cicloRepository.getIdCiclo();
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            Integer idUsuario = (Integer) arrayId[0];
            String codigoAmbiente = (String) arrayId[1];
            Object[] verificacion = (Object[]) transaccionRepository.existeNroMovimiento(idUsuario, codigoAmbiente, UtilsDominio.PEDIDO, idCiclo, request.getPedidoObjeto().getNroMovimiento());
            if(verificacion ==null || verificacion[0].equals(0l)) {
                Integer idProveedo = usuarioRepository.getIdUsuarioProveedor(request.getPedidoObjeto().getCodigoProveedor(), UtilsDominio.TIPO_USUARIO_PROVEEDOR, codigoAmbiente);
                transaccion = new Transaccion();
                String sid = idCiclo + "f" + codigoAmbiente + "f" + idUsuario + "f" + request.getPedidoObjeto().getNroMovimiento();//getSecuencialTransaccion().toString();
                transaccion.setId(sid);
                transaccion.setIdCiclo(idCiclo);
                transaccion.setCodigoDominio(UtilsDominio.PEDIDO);
                transaccion.setCodigoValor(UtilsDominio.PEDIDO_SOLICITUD);
                transaccion.setCodigoValorPago(UtilsDominio.TIPO_PAGO_PAGADO);//Cambiable cuando se adicione la funcion de pago
                transaccion.setNroMovimiento(request.getPedidoObjeto().getNroMovimiento());
                transaccion.setIdUsuarioInicio(idUsuario);
                transaccion.setCodigoAmbienteInicio(codigoAmbiente);
                transaccion.setFechaInicio(UtilsGeneral.convertirFecha(request.getPedidoObjeto().getFechaMovimiento()));
                transaccion.setIdUsuarioFin(idProveedo);
                transaccion.setCodigoAmbienteFin(null);
                transaccion.setFechaFin(null);
                transaccion.setObservacion(request.getPedidoObjeto().getObservacion());
                transaccion.setFechaAlta(fecha);
                transaccion.setOperadorAlta(String.valueOf(idUsuario));
                transaccionRepository.save(transaccion);
                request.getPedidoObjeto().setId(transaccion.getId());
                for (PedidoDetalle pedido : request.getPedidoObjeto().getLista()) {
                    detalle = new DetalleTransaccion();
                    String uid = transaccion.getId() + getSecuencialDetalleTransaccion().toString();
                    detalle.setId(uid);
                    detalle.setIdTransaccion(transaccion.getId());
                    BigDecimal precioVenta = articuloRepository.getPrecioVentaPorCodigo(pedido.getCodigoArticulo());
                    detalle.setCodigoArticulo(pedido.getCodigoArticulo());
                    detalle.setCantidad(pedido.getCantidad());
                    detalle.setPrecio(pedido.getPrecio());
                    detalle.setPrecioSistema(precioVenta);
                    detalle.setObservacion(pedido.getObservacion());
                    detalle.setOperadorAlta(String.valueOf(idUsuario));
                    detalle.setFechaAlta(fecha);
                    detalleTransaccionRepository.save(detalle);
                    pedido.setId(detalle.getId());
                }
                response.setPedidoObjeto(request.getPedidoObjeto());
                response.setRespuesta(true);
            } else {
                response.setMensaje("Ya existe el nro movimiento " + verificacion[0] + ", se creo el " + verificacion[1]);
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.setPedidoObjeto(null);
            response.setMensaje("Error al guardar la transaccion ");
        }
        if(!response.isRespuesta())
            throw new Exception(response.getMensaje());
        return response;
    }

    @Override
    public PedidoResponseList listaSolicitud(String token) {
        PedidoResponseList response = new PedidoResponseList();
        try {
            String codigoAmbiente = credencialRepository.getCodigoAmbienteByToken(token);
            Integer idCiclo = cicloRepository.getIdCiclo();
            List<PedidoObjeto> lista = transaccionRepository.listaTransacciones(codigoAmbiente, UtilsDominio.PEDIDO, UtilsDominio.PEDIDO_SOLICITUD, idCiclo, UtilsDominio.TIPO_USUARIO_PROVEEDOR);
            for (PedidoObjeto pedido : lista) {
                pedido.setLista(detalleTransaccionRepository.listaDetalle(pedido.getId()));
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
    public PedidoResponse actualizar(String token, PedidoRequest request) throws Exception{
        PedidoResponse response = new PedidoResponse();
        Transaccion transaccion = null;
        DetalleTransaccion detalle = null;
        Date fecha = new Date();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            Integer idUsuario = (Integer) arrayId[0];
            String codigoAmbiente = (String) arrayId[1];
            Integer idProveedo = usuarioRepository.getIdUsuarioProveedor(request.getPedidoObjeto().getCodigoProveedor(), UtilsDominio.TIPO_USUARIO_PROVEEDOR, codigoAmbiente);
            transaccion = transaccionRepository.findOne(request.getPedidoObjeto().getId());
            if(transaccion != null) {
                transaccion.setIdUsuarioFin(idProveedo);
                transaccion.setObservacion(request.getPedidoObjeto().getObservacion());
                transaccion.setFechaActualizacion(fecha);
                transaccion.setOperadorActualizacion(String.valueOf(idUsuario));
                transaccionRepository.save(transaccion);
                request.getPedidoObjeto().setId(transaccion.getId());
                List<String> listaActualizada = new ArrayList<>();
                for (PedidoDetalle pedido : request.getPedidoObjeto().getLista()) {
                    if (pedido.getId() != null) {
                        detalle = detalleTransaccionRepository.findOne(pedido.getId());
                        listaActualizada.add(pedido.getId());
                        detalle.setCodigoArticulo(pedido.getCodigoArticulo());
                        detalle.setCantidad(pedido.getCantidad());
                        detalle.setPrecio(pedido.getPrecio());
                        detalle.setObservacion(pedido.getObservacion());
                        detalle.setOperadorActualizacion(String.valueOf(idUsuario));
                        detalle.setFechaActualizacion(fecha);
                        detalleTransaccionRepository.save(detalle);
                    }
                }

                List<DetalleTransaccion> listBaja = detalleTransaccionRepository.findByIdNotInAndFechaBajaIsNull(listaActualizada);

                for (DetalleTransaccion de : listBaja) {
                    de.setFechaBaja(fecha);
                    de.setOperadorBaja(String.valueOf(idUsuario));
                    detalleTransaccionRepository.save(de);
                }

                for (PedidoDetalle pedido : request.getPedidoObjeto().getLista()) {
                    if (pedido.getId() == null) {
                        String idDetalleExistente = detalleTransaccionRepository.existeArticuloEnTransaccion(transaccion.getId(), pedido.getCodigoArticulo());
                        if (idDetalleExistente == null) {
                            detalle = new DetalleTransaccion();
                            String uid = transaccion.getId() + getSecuencialDetalleTransaccion().toString();
                            detalle.setId(uid);
                            detalle.setIdTransaccion(transaccion.getId());
                            BigDecimal precioVenta = articuloRepository.getPrecioVentaPorCodigo(pedido.getCodigoArticulo());
                            detalle.setCodigoArticulo(pedido.getCodigoArticulo());
                            detalle.setCantidad(pedido.getCantidad());
                            detalle.setPrecio(pedido.getPrecio());
                            detalle.setPrecioSistema(precioVenta);
                            detalle.setObservacion(null);
                            detalle.setOperadorAlta(String.valueOf(idUsuario));
                            detalle.setFechaAlta(fecha);
                            detalleTransaccionRepository.save(detalle);
                            pedido.setId(detalle.getId());
                            listaActualizada.add(pedido.getId());
                        } else {
                            response.setMensaje("Esta volviendo a insertar un articulo");
                        }
                    }
                }

                if (response.getMensaje() == null) {
                    response.setPedidoObjeto(request.getPedidoObjeto());
                    response.setRespuesta(true);
                }
            } else {
                response.setMensaje("No se encontro la transaccion");
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.setPedidoObjeto(null);
            response.setMensaje("Error al actualizar la transaccion ");
        }
        if(!response.isRespuesta())
            throw new Exception(response.getMensaje());
        return response;
    }

    @Override
    public ServResponse eliminar(String token, String idTransaccion) throws Exception {
        ServResponse response = new ServResponse();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if(arrayId != null) {
                Integer idUsuario = (Integer) arrayId[0];//String codigoAmbiente = (String) arrayId[1];
                Date fecha = new Date();
                Transaccion transaccion = transaccionRepository.findOne(idTransaccion);
                transaccion.setFechaBaja(fecha);
                transaccion.setOperadorBaja(String.valueOf(idUsuario));
                transaccionRepository.save(transaccion);//transaccionRepository.bajaTransaccion(idTransaccion, codigoAmbiente, String.valueOf(idUsuario), fecha);
                List<DetalleTransaccion> detalles = detalleTransaccionRepository.findByIdTransaccionAndFechaBajaIsNull(transaccion.getId());//detalleTransaccionRepository.bajaDetalleTransaccion(idTransaccion, String.valueOf(idUsuario), fecha);
                for(DetalleTransaccion detalle : detalles) {
                    detalle.setFechaBaja(fecha);
                    detalle.setOperadorBaja(String.valueOf(idUsuario));
                    detalleTransaccionRepository.save(detalle);
                }
                response.setRespuesta(true);
            } else {
                response.setMensaje("Las credenciales estan vencidas, ingrese desde el login nuevamente");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al dar de baja la transaccion");
        }
        if(!response.isRespuesta())
            throw new Exception(response.getMensaje());
        return response;
    }


    @Override
    public PedidoResponse obtener(String token, String id) {
        PedidoResponse response = new PedidoResponse();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if(arrayId != null) {
                PedidoObjeto pedidoObjeto = transaccionRepository.getPedidoObjeto(id);
                if(pedidoObjeto != null) {
                    pedidoObjeto.setLista(detalleTransaccionRepository.listaDetalle(id));
                    response.setPedidoObjeto(pedidoObjeto);
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


    @Override
    public ServResponse confirmarLlegada(String token, String id) throws Exception {
        ServResponse response = new ServResponse();
        Transaccion transaccion = null;
        Date fecha = null;
        try {
            fecha = new Date();
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if(arrayId != null) {
                Integer idUsuario = (Integer) arrayId[0];
                transaccion = transaccionRepository.getTransaccion(id);
                if (transaccion != null) {
                    if (transaccion.getCodigoValor().equals(UtilsDominio.PEDIDO_SOLICITUD)) {
                        transaccion.setCodigoValor(UtilsDominio.PEDIDO_LLEGADA);
                        transaccion.setFechaActualizacion(fecha);
                        transaccion.setOperadorActualizacion(String.valueOf(idUsuario));
                        transaccionRepository.save(transaccion);
                        List<DetalleTransaccion> detalles = detalleTransaccionRepository.findByIdTransaccionAndFechaBajaIsNull(id);
                        for (DetalleTransaccion dt : detalles) {
                            dt.setOperadorActualizacion(String.valueOf(idUsuario));
                            dt.setFechaActualizacion(fecha);
                            detalleTransaccionRepository.save(dt);
                            Inventario inventario = inventarioRepository.getInventario(transaccion.getCodigoAmbienteInicio(), dt.getCodigoArticulo());
                            if(inventario == null) {
                                inventario = new Inventario();
                                inventario.setCodigoAmbiente(transaccion.getCodigoAmbienteInicio());
                                inventario.setCodigoArticulo(dt.getCodigoArticulo());
                                inventario.setExistencia(dt.getCantidad());
                                inventario.setFechaAlta(fecha);
                                inventario.setOperadorAlta(String.valueOf(idUsuario));
                            } else {
                                Integer cantidad = inventario.getExistencia() + dt.getCantidad();
                                inventario.setExistencia(cantidad);
                                inventario.setFechaActualizacion(fecha);
                                inventario.setOperadorActualizacion(String.valueOf(idUsuario));
                            }
                            inventarioRepository.save(inventario);
                        }
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
            response.setMensaje("Error al confirmar la llegada ");
        }
        if(!response.isRespuesta())
            throw new Exception(response.getMensaje());
        return response;
    }

    @Override
    public ServResponse cancelarLlegada(String token, String id) throws Exception {
        ServResponse response = new ServResponse();
        Transaccion transaccion = null;
        Date fecha = null;
        try {
            fecha = new Date();
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if(arrayId != null) {
                Integer idUsuario = (Integer) arrayId[0];
                transaccion = transaccionRepository.getTransaccion(id);
                if (transaccion != null) {
                    if (transaccion.getCodigoValor().equals(UtilsDominio.PEDIDO_LLEGADA)) {
                        transaccion.setCodigoValor(UtilsDominio.PEDIDO_SOLICITUD);
                        transaccion.setFechaActualizacion(fecha);
                        transaccion.setOperadorActualizacion(String.valueOf(idUsuario));
                        transaccionRepository.save(transaccion);
                        List<DetalleTransaccion> detalles = detalleTransaccionRepository.findByIdTransaccionAndFechaBajaIsNull(id);
                        for (DetalleTransaccion detalle : detalles) {
                            Inventario inventario = inventarioRepository.getInventario(transaccion.getCodigoAmbienteInicio(), detalle.getCodigoArticulo());
                            Integer cantidad = inventario.getExistencia() - detalle.getCantidad();
                            if(cantidad < 0) {
                                response.setMensaje("Error no existe suficiente inventario para cancelar esta llegada");
                            }
                            inventario.setExistencia(cantidad);
                            inventario.setFechaActualizacion(fecha);
                            inventario.setOperadorActualizacion(String.valueOf(idUsuario));
                            inventarioRepository.save(inventario);
                        }
                        if(response.getMensaje() == null) {
                            response.setRespuesta(true);
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
        } catch(Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al confirmar la llegada ");
        }
        if(!response.isRespuesta())
            throw new Exception(response.getMensaje());
        return response;
    }

    @Override
    public PedidoResponseList listaLlegadas(String token) {
        PedidoResponseList response = new PedidoResponseList();
        try {
            String codigoAmbiente = credencialRepository.getCodigoAmbienteByToken(token);
            Integer idCiclo = cicloRepository.getIdCiclo();
            List<PedidoObjeto> lista = transaccionRepository.listaTransacciones(codigoAmbiente, UtilsDominio.PEDIDO, UtilsDominio.PEDIDO_LLEGADA, idCiclo, UtilsDominio.TIPO_USUARIO_PROVEEDOR);
            for (PedidoObjeto pedido : lista) {
                pedido.setLista(detalleTransaccionRepository.listaDetalle(pedido.getId()));
            }
            response.setList(lista);
            response.setRespuesta(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al generar la lista de Pedidos");
        }
        return response;
    }
}
