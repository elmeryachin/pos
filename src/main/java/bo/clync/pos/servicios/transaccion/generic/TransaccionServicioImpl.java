package bo.clync.pos.servicios.transaccion.generic;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.*;
import bo.clync.pos.arquetipo.tablas.DetalleTransaccion;
import bo.clync.pos.arquetipo.tablas.Inventario;
import bo.clync.pos.arquetipo.tablas.Transaccion;
import bo.clync.pos.repository.acceso.ConectadoRepository;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.articulo.ArticuloRepository;
import bo.clync.pos.repository.common.CicloRepository;
import bo.clync.pos.repository.common.InventarioRepository;
import bo.clync.pos.repository.common.UsuarioRepository;
import bo.clync.pos.repository.transaccion.pedido.DetalleTransaccionRepository;
import bo.clync.pos.repository.transaccion.pedido.TransaccionRepository;
import bo.clync.pos.utilitarios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class TransaccionServicioImpl implements TransaccionServicio {
    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;
    @Autowired
    private TransaccionRepository transaccionRepository;
    @Autowired
    private CicloRepository cicloRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ConectadoRepository conectadoRepository;
    @Autowired
    private ArticuloRepository articuloRepository;
    @Autowired
    private DetalleTransaccionRepository detalleTransaccionRepository;
    @Autowired
    private InventarioRepository inventarioRepository;
    @Autowired
    private UsuarioAmbienteCredencialRepository usuarioAmbienteCredencialRepository;
    @PersistenceContext
    private EntityManager em;

    private Object[] getConectados(String token) {
        Integer id = conectadoRepository.obtenerIdUsuarioAmbienteCredencial(token);
        return (Object[]) credencialRepository.origenCredencial(id);
    }

    private java.math.BigInteger getSecuencialTransaccion() {
        return (java.math.BigInteger) em.createNativeQuery("SELECT nextval('transaccion_id_seq')").getSingleResult();
    }

    private java.math.BigInteger getSecuencialDetalleTransaccion11() {
        return (java.math.BigInteger) em.createNativeQuery("SELECT nextval('detalle_transaccion_id_seq')").getSingleResult();
    }

    private Long getContadorDetalle(String idTransaccion) {
        // cuantos detalles existen por id transaccion
        // No se discrimina nada.
        Long count = detalleTransaccionRepository.getDetalleTransaccionMaximoAll(idTransaccion) + 1l;
        return count;
    }

    @Override
    public TransaccionResponseInit init(String token, String dominio) {
        TransaccionResponseInit init = new TransaccionResponseInit();
        try {
            Object[] o = getConectados(token);
            Integer nro = transaccionRepository.initMovimiento((Long) o[0], (String) o[1], dominio, cicloRepository.getIdCiclo());
            nro = nro == null ? 1 : nro + 1;
            init.setNroMovimiento(nro);
            init.setFechaMovimiento(UtilsGeneral.fechaActual());
            init.setRespuesta(true);
        } catch (Exception e) {
            e.printStackTrace();
            init.setMensaje("Error al generar el nro de movimiento para " + dominio);
        }
        return init;
    }

    /**
     * Completa por condicion de dominio los campos que deben ser editados
     *
     * @param dominio
     * @param request
     * @param transaccion
     * @param codigoAmbiente
     */
    private void getTransaccion(String dominio, TransaccionRequest request, Transaccion transaccion, String codigoAmbiente) {
        if (dominio.equals(UtilsDominio.PEDIDO)) {
            System.out.println("@#@@@@@@@@@@@@@@@@@@@@@@ ");
            System.out.println(request.getTransaccionObjeto().getCodigo());
            System.out.println(UtilsDominio.TIPO_USUARIO_PROVEEDOR);
            System.out.println(codigoAmbiente);
            System.out.println(usuarioRepository.getIdUsuarioConAmbiente(request.getTransaccionObjeto().getCodigo(), UtilsDominio.TIPO_USUARIO_PROVEEDOR, codigoAmbiente));
            transaccion.setIdUsuarioFin(usuarioRepository.getIdUsuarioConAmbiente(request.getTransaccionObjeto().getCodigo(), UtilsDominio.TIPO_USUARIO_PROVEEDOR, codigoAmbiente));
        } else if (dominio.equals(UtilsDominio.TRANSFERENCIA)) {
            transaccion.setCodigoAmbienteFin(request.getTransaccionObjeto().getCodigo());
            Long idUsuarioFin = this.usuarioAmbienteCredencialRepository.getIdUsuarioByCodigoAmbiente(request.getTransaccionObjeto().getCodigo());
            transaccion.setIdUsuarioFin(idUsuarioFin);
        } else if (dominio.equals(UtilsDominio.TRANSFERENCIA_NLL)) {
            transaccion.setCodigoAmbienteFin(request.getTransaccionObjeto().getCodigo());
            Long idUsuarioFin = this.usuarioAmbienteCredencialRepository.getIdUsuarioByCodigoAmbiente(request.getTransaccionObjeto().getCodigo());
            transaccion.setIdUsuarioFin(idUsuarioFin);
        } else if (dominio.equals(UtilsDominio.VENTA)) {
            transaccion.setIdUsuarioFin(usuarioRepository.getIdUsuarioCliente(request.getTransaccionObjeto().getCodigo(), UtilsDominio.TIPO_USUARIO_CLIENTE));
        } else if (dominio.equals(UtilsDominio.SOLICITUD_INTERNA)) {
            Long idUsuarioFin = this.usuarioAmbienteCredencialRepository.getIdUsuarioByCodigoAmbiente(request.getTransaccionObjeto().getCodigo());
            transaccion.setIdUsuarioFin(idUsuarioFin);
            transaccion.setCodigoAmbienteFin(request.getTransaccionObjeto().getCodigo());
        }

        transaccion.setObservacion(request.getTransaccionObjeto().getObservacion());
    }

    @Override
    public String getIdTransaccion( String dominio, String nroMovimiento, String token ) {
        Integer idCiclo = this.cicloRepository.getIdCiclo();
        Object[] arrayId = (Object[]) this.credencialRepository.getIdUsuarioByToken(token);
        Long idUsuario = (Long) arrayId[0];
        String codigoAmbiente = (String) arrayId[1];
        return idCiclo + "f" + codigoAmbiente + "f" + idUsuario + "f" + dominio + nroMovimiento;
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
                    Long idUsuario = (Long) arrayId[0];
                    String codigoAmbiente = (String) arrayId[1];
                    //Parche para crear registro de no llegadas tras una transferencia recibida con edicion
                    Object[] verificacion = null;
                    if(!valor.equals(UtilsDominio.TRANSFERENCIA_NLL_RECIBIR_NO_LLEGO)) {
                        verificacion = (Object[]) this.transaccionRepository.existeNroMovimiento(idUsuario, codigoAmbiente, dominio, idCiclo, request.getTransaccionObjeto().getNroMovimiento());
                    }
                    if (verificacion == null || verificacion[0].equals(0l)) {
                        transaccion = new Transaccion();
                        String sid = idCiclo + "f" + codigoAmbiente + "f" + idUsuario + "f" + dominio + request.getTransaccionObjeto().getNroMovimiento();

                        //Parche para crear registro de no llegadas tras una transferencia recibida con edicion
                        if(valor.equals(UtilsDominio.TRANSFERENCIA_NLL_RECIBIR_NO_LLEGO)) {
                        	//Asignacion de id con el relacionado con sufijo "A"
                        	sid = request.getTransaccionObjeto().getId() + "A";
                        }
                        
                        transaccion.setId(sid);
                        transaccion.setIdCiclo(idCiclo);
                        transaccion.setCodigoDominio(dominio);
                        transaccion.setCodigoValor(valor);
                        transaccion.setCodigoValorPago(tipoPago);
                        transaccion.setNroMovimiento(request.getTransaccionObjeto().getNroMovimiento());
                        transaccion.setIdUsuarioInicio(idUsuario);
                        transaccion.setCodigoAmbienteInicio(codigoAmbiente);
                        transaccion.setFechaInicio(UtilsGeneral.convertirFecha(request.getTransaccionObjeto().getFechaMovimiento()));

                        transaccion.setCantidad(request.getTransaccionObjeto().getCantidad());
                        transaccion.setPrecio(request.getTransaccionObjeto().getPrecio().add(new BigDecimal("0.00")));

                        this.getTransaccion(dominio, request, transaccion, codigoAmbiente);

                        transaccion.setFechaAlta(fecha);
                        transaccion.setOperadorAlta(String.valueOf(idUsuario));

                        transaccion.setFechaBaja(null);
                        transaccion.setOperadorBaja(null);
                        this.transaccionRepository.save(transaccion);
                        request.getTransaccionObjeto().setId(transaccion.getId());

                        Long idDetalle = getContadorDetalle(transaccion.getId());
                        Integer cantidadDetalle = 0 ;
                        BigDecimal precioDetalle =  new BigDecimal("0.00");
                        for (TransaccionDetalle pedido : request.getTransaccionObjeto().getLista()) {
                            detalle = new DetalleTransaccion();
                            String uid = transaccion.getId() + idDetalle.toString();
                            idDetalle = idDetalle + 1L;
                            //String uid = transaccion.getId() + getSecuencialDetalleTransaccion().toString();
                            System.out.println("uid:::: " + uid);
                            System.out.println("pedido.getCantidad():: " + pedido.getCantidad());
                            System.out.println("pedido.getPrecio(): " + pedido.getPrecio());
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
                            cantidadDetalle = cantidadDetalle + detalle.getCantidad();
                            precioDetalle = precioDetalle.add( detalle.getPrecio().multiply(new BigDecimal(detalle.getCantidad())) );
                        }
                        System.out.println("cantidadDetalle : " + cantidadDetalle);
                        System.out.println("transaccion.getCantidad(): " + transaccion.getCantidad());

                        System.out.println("precioDetalle " + precioDetalle);
                        System.out.println("transaccion.getPrecio() " + transaccion.getPrecio());

                        // Validacion de monto y cantidad entre el detalle y la cabecera principal
                        if ( !cantidadDetalle.equals(transaccion.getCantidad())
                                || !precioDetalle.subtract(transaccion.getPrecio()).toString().equals("0.00") ) {
                            throw new Exception("Cantidad y/o Montos distintos (principal) : Precio: " + transaccion.getPrecio() + " Cantidad: " + transaccion.getCantidad());
                        }

                        String msgError = getInventario(this.inventarioRepository,
                                idUsuario, fecha,
                                codigoAmbiente, transaccion.getCodigoAmbienteFin(),
                                dominio, valor, request.getTransaccionObjeto().getLista(),
                                UtilsConstante.INVENTARIO_ADD);

                        if(msgError != null) throw new Exception(msgError);

                        response.setTransaccionObjeto(request.getTransaccionObjeto());
                        response.setRespuesta(true);
                    } else {
                        response.setMensaje("Ya existe el nro movimiento " + verificacion[0] + ", se creo el " + verificacion[1]);
                    }
                } else {
                    response.setMensaje("Adicione al menos un articulo al detalle");
                }
            } else {
                response.setMensaje("La transaccion recibida se encuentra vacia.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setTransaccionObjeto(null);
            response.setMensaje(e.getMessage());
        }
        if (!response.isRespuesta())
            throw new Exception(response.getMensaje());
        return response;
    }

    private String getInventario(InventarioRepository repository,
                                 Long idUsuario, Date fecha,
                                 String ambienteOrigen, String ambienteDestino,
                                 String dominio, String valor,
                                 List<TransaccionDetalle> list,
                                 Integer operador) throws Exception {
        Inventario inventario = null;
        String msgError = null;
        for (TransaccionDetalle detalle : list) {
            inventario = repository.getInventario(ambienteOrigen, detalle.getCodigoArticulo());
            if (dominio.equals(UtilsDominio.PEDIDO) && valor.equals(UtilsDominio.PEDIDO_SOLICITUD)) {
                if (inventario == null) {
                    inventario = new Inventario();
                    inventario.setCodigoAmbiente(ambienteOrigen);
                    inventario.setCodigoArticulo(detalle.getCodigoArticulo());
                    inventario.setExistencia(0);
                    inventario.setPorLlegar(detalle.getCantidad());
                    inventario.setFechaAlta(fecha);
                    inventario.setOperadorAlta(String.valueOf(idUsuario));
                } else {
                    inventario.setPorLlegar(UtilsOperacion.getNumeroNoNulo(inventario.getPorLlegar()) + operador * detalle.getCantidad());
                }
                repository.save(inventario);

            } else if (dominio.equals(UtilsDominio.TRANSFERENCIA) && valor.equals(UtilsDominio.TRANSFERENCIA_ENVIO)) {
                inventario.setPorEntregar(UtilsOperacion.getNumeroNoNulo(inventario.getPorEntregar()) + operador * detalle.getCantidad());
                inventario.setExistencia(inventario.getExistencia() - operador * detalle.getCantidad());
                repository.save(inventario);

                inventario = repository.getInventario(ambienteDestino, detalle.getCodigoArticulo());
                if (inventario == null) {
                    inventario = new Inventario();
                    inventario.setCodigoAmbiente(ambienteDestino);
                    inventario.setCodigoArticulo(detalle.getCodigoArticulo());
                    inventario.setExistencia(0);
                    inventario.setFechaAlta(fecha);
                    inventario.setOperadorAlta(String.valueOf(idUsuario));
                }
                inventario.setPorRecibir(UtilsOperacion.getNumeroNoNulo(inventario.getPorRecibir()) + operador * detalle.getCantidad());
                repository.save(inventario);
            } else if (dominio.equals(UtilsDominio.TRANSFERENCIA_NLL) && valor.equals(UtilsDominio.TRANSFERENCIA_NLL_RECIBIR_NO_LLEGO)) {
            	// NOT IMPLEMENTS
            	inventario.setExistencia( inventario.getExistencia() - operador * detalle.getCantidad() );
            	repository.save( inventario );
            	
            	inventario = repository.getInventario( ambienteDestino, detalle.getCodigoArticulo() );
            	inventario.setExistencia( inventario.getExistencia() + operador * detalle.getCantidad() );
            	repository.save( inventario );
            	
            } else if (dominio.equals(UtilsDominio.VENTA) && valor.equals(UtilsDominio.VENTA_REALIZADA)) {
                inventario.setExistencia(inventario.getExistencia() - operador * detalle.getCantidad());
                repository.save(inventario);
                if (inventario.getExistencia() < 0) msgError = "No existe suficientes articulos";
            }
        }

        return msgError;
    }

    @Override
    public TransaccionResponse actualizar(String token, TransaccionRequest request, String dominio) throws Exception {
        TransaccionResponse response = new TransaccionResponse();
        Transaccion transaccion = null;
        DetalleTransaccion detalle = null;
        Date fecha = new Date();
        try {
            Object[] arrayId = (Object[]) this.credencialRepository.getIdUsuarioByToken(token);
            Long idUsuario = (Long) arrayId[0];
            String codigoAmbiente = (String) arrayId[1];
            transaccion = this.transaccionRepository.findOne(request.getTransaccionObjeto().getId());

            if (transaccion.getFechaBaja() == null) {
                List<TransaccionDetalle> listaDetalle = this.detalleTransaccionRepository.listaDetalle(request.getTransaccionObjeto().getId());
                this.getInventario(this.inventarioRepository,
                        idUsuario, fecha,
                        codigoAmbiente, transaccion.getCodigoAmbienteFin(),
                        transaccion.getCodigoDominio(), transaccion.getCodigoValor(),
                        listaDetalle, UtilsConstante.INVENTARIO_REMOVE);

                if (transaccion != null) {

                    this.getTransaccion(dominio, request, transaccion, codigoAmbiente);

                    transaccion.setFechaActualizacion(fecha);
                    transaccion.setOperadorActualizacion(String.valueOf(idUsuario));

                    transaccion.setPrecio(request.getTransaccionObjeto().getPrecio().add(new BigDecimal("0.00")));
                    transaccion.setCantidad(request.getTransaccionObjeto().getCantidad());

                    this.transaccionRepository.save(transaccion);
                    request.getTransaccionObjeto().setId(transaccion.getId());

                    BigDecimal precioDetalle = new BigDecimal(("0.00"));
                    Integer cantidadDetalle = 0;
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

                            precioDetalle = precioDetalle.add(detalle.getPrecio().multiply(new BigDecimal(detalle.getCantidad())));
                            cantidadDetalle = cantidadDetalle + detalle.getCantidad();
                        }
                    }

                    List<DetalleTransaccion> listBaja = this.detalleTransaccionRepository.findByIdTransaccionAndIdNotInAndFechaBajaIsNull( transaccion.getId(), listaActualizada);

                    System.out.println("listaBaja: " + listBaja.size());

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
                                detalle.setId(transaccion.getId() + getContadorDetalle(transaccion.getId()).toString());
                                //detalle.setId(transaccion.getId() + this.getSecuencialDetalleTransaccion().toString());
                                detalle.setIdTransaccion(transaccion.getId());
                                detalle.setCodigoArticulo(de.getCodigoArticulo());
                                detalle.setCantidad(de.getCantidad());
                                detalle.setPrecio(de.getPrecio());
                                detalle.setPrecioSistema(this.articuloRepository.getPrecioVentaPorCodigo(de.getCodigoArticulo()));
                                detalle.setObservacion(de.getObservacion());
                                detalle.setOperadorAlta(String.valueOf(idUsuario));
                                detalle.setFechaAlta(fecha);
                                detalleTransaccionRepository.save(detalle);

                                precioDetalle = precioDetalle.add(detalle.getPrecio().multiply(new BigDecimal(detalle.getCantidad())));
                                cantidadDetalle = cantidadDetalle + detalle.getCantidad();

                                de.setId(detalle.getId());
                                listaActualizada.add(de.getId());
                            } else {
                                response.setMensaje("Esta volviendo a insertar un articulo");
                            }
                        }
                    }

                    if ( !cantidadDetalle.equals(transaccion.getCantidad())
                            || !precioDetalle.subtract(transaccion.getPrecio()).toString().equals("0.00")) {
                        throw new Exception("Cantidad y/o Montos distintos (principal) : Precio: " + transaccion.getPrecio() + " Cantidad: " + transaccion.getCantidad());
                    }

                    String msgError = this.getInventario(this.inventarioRepository,
                            idUsuario, fecha,
                            codigoAmbiente, transaccion.getCodigoAmbienteFin(),
                            transaccion.getCodigoDominio(), transaccion.getCodigoValor(),
                            request.getTransaccionObjeto().getLista(),
                            UtilsConstante.INVENTARIO_ADD);
                    response.setMensaje(msgError);
                    if (response.getMensaje() == null) {
                        response.setTransaccionObjeto(request.getTransaccionObjeto());
                        response.setRespuesta(true);
                    }
                } else {
                    response.setMensaje("No se encontro la transaccion");
                }
            } else {
                response.setMensaje("Transaccion no editable   (Se encuentra dada de baja)");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setTransaccionObjeto(null);
            response.setMensaje("Error al actualizar la transaccion ");
        }
        if (!response.isRespuesta())
            throw new Exception(response.getMensaje());
        return response;
    }

    @Override
    public ServResponse eliminar(String token, String idTransaccion, String valor) throws Exception {
        ServResponse response = new ServResponse();
        try {
            Object[] arrayId = (Object[]) this.credencialRepository.getIdUsuarioByToken(token);
            if (arrayId != null) {
                Long idUsuario = (Long) arrayId[0];
                String codigoAmbiente = (String) arrayId[1];
                Date fecha = new Date();
                Transaccion transaccion = this.transaccionRepository.findByIdAndFechaBajaIsNull(idTransaccion);
                if (transaccion.getCodigoValor().equals(valor)) {
                    transaccion.setFechaBaja(fecha);
                    transaccion.setOperadorBaja(String.valueOf(idUsuario));
                    this.transaccionRepository.save(transaccion);
                    List<DetalleTransaccion> detalles = this.detalleTransaccionRepository.findByIdTransaccionAndFechaBajaIsNull(transaccion.getId());
                    List<TransaccionDetalle> listAux = new ArrayList<>();
                    for (DetalleTransaccion detalle : detalles) {
                        TransaccionDetalle aux = new TransaccionDetalle();
                        detalle.setFechaBaja(fecha);
                        detalle.setOperadorBaja(String.valueOf(idUsuario));
                        this.detalleTransaccionRepository.save(detalle);
                        aux.setCantidad(detalle.getCantidad());
                        aux.setCodigoArticulo(detalle.getCodigoArticulo());
                        listAux.add(aux);
                    }

                    this.getInventario(this.inventarioRepository,
                            idUsuario, fecha,
                            codigoAmbiente, transaccion.getCodigoAmbienteFin(),
                            transaccion.getCodigoDominio(), transaccion.getCodigoValor(),
                            listAux,
                            UtilsConstante.INVENTARIO_REMOVE);
                    response.setRespuesta(true);
                } else {
                    response.setMensaje("No se puede dar de baja la transaccion , los permitidos {PEDIDO SOLICITUD, TRANSFERENCIA ENVIADO}");
                }
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
        List<TransaccionObjeto> listAux = new ArrayList();
        try {
            String codigoAmbiente = this.credencialRepository.getCodigoAmbienteByToken(token);
            Integer idCiclo = this.cicloRepository.getIdCiclo();
            List<TransaccionObjeto> lista = null;
            if (dominio.equals(UtilsDominio.PEDIDO)) {
                lista = this.transaccionRepository.listaPedidos(codigoAmbiente, dominio, valor, idCiclo, tipoUsuario);
            } else if (dominio.equals(UtilsDominio.TRANSFERENCIA)
                    && valor.equals(UtilsDominio.TRANSFERENCIA_ENVIO)) {
                lista = this.transaccionRepository.listaTransferenciasEnviosPorOrigen(codigoAmbiente, dominio, valor, idCiclo);
            } else if (dominio.equals(UtilsDominio.TRANSFERENCIA)
                    && valor.equals(UtilsDominio.TRANSFERENCIA_ENVIO_DESTINO_AUX)) {
                lista = this.transaccionRepository.listaTransferenciasEnviosPorDestino(codigoAmbiente, dominio, UtilsDominio.TRANSFERENCIA_ENVIO, idCiclo);
            } else if (dominio.equals(UtilsDominio.TRANSFERENCIA)
                    && valor.equals(UtilsDominio.TRANSFERENCIA_RECIBIR)) {
                lista = this.transaccionRepository.listaTransferenciasRecibidos(codigoAmbiente, dominio, valor, UtilsDominio.TRANSFERENCIA_RECIBIR_EDIT, idCiclo);
            }  else if (dominio.equals(UtilsDominio.TRANSFERENCIA)
                    && valor.equals(UtilsDominio.TRANSFERENCIA_RECIBIR_CONF)) {
                lista = this.transaccionRepository.listaTransferenciasEnviosPorOrigen(codigoAmbiente, dominio, valor, idCiclo);
            }  else if (dominio.equals(UtilsDominio.TRANSFERENCIA)
                    && valor.equals(UtilsDominio.TRANSFERENCIA_RECIBIR_ORIGEN_AUX)) {
                lista = this.transaccionRepository.listaTransferenciasRecibidosPorOrigen(codigoAmbiente, dominio, UtilsDominio.TRANSFERENCIA_RECIBIR, UtilsDominio.TRANSFERENCIA_RECIBIR_EDIT, idCiclo);
            } else if (dominio.equals(UtilsDominio.VENTA)
                    && valor.equals(UtilsDominio.VENTA_REALIZADA)) {
                lista = this.transaccionRepository.listaVentas(codigoAmbiente, dominio, valor, idCiclo, tipoUsuario);
            } else if (dominio.equals(UtilsDominio.VENTA)
                    && valor.equals(UtilsDominio.VENTA_CONFIRMADA)) {
                System.out.println(String.format("%s, %s, %s, %s, %s, ", codigoAmbiente, dominio, valor, idCiclo, tipoUsuario));
                lista = this.transaccionRepository.listaVentas(codigoAmbiente, dominio, valor, idCiclo, tipoUsuario);
            } else if (dominio.equals(UtilsDominio.SOLICITUD_INTERNA)) {
                System.out.println(String.format("%s, %s, %s, %s", codigoAmbiente, dominio, valor, idCiclo));
                lista = this.transaccionRepository.listaTransferenciasEnviosPorOrigen(codigoAmbiente, dominio, valor, idCiclo);
            } else if (dominio.equals(UtilsDominio.SOLICITUD_INTERNA_AUX)) {
                System.out.println(String.format("%s, %s, %s",  dominio, valor, idCiclo));
                lista = this.transaccionRepository.listaTransferenciasEnviosTodos(dominio, valor, idCiclo);
            }
            System.out.println("list transaccion > " + lista);
            for (TransaccionObjeto pedido : lista) {
                pedido.setLista(this.detalleTransaccionRepository.listaDetalle(pedido.getId()));
                System.out.println("lista detalle    > " + pedido.getLista());
                if (pedido.getLista() == null || pedido.getLista().size() == 0) {
                    listAux.add(pedido);
                }
            }
            lista.removeAll(listAux);
            response.setList(lista);
            response.setRespuesta(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al generar la lista de Pedidos");
        }
        return response;
    }

    @Override
    public TransaccionResponse obtener(String token, String id, String dominio, String valor) {
        TransaccionResponse response = new TransaccionResponse();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if (arrayId != null) {
                TransaccionObjeto tra = null;
                if( dominio.equals( UtilsDominio.TRANSFERENCIA )) {
                    tra = transaccionRepository.getTransaccionObjetoAmbiente(id, dominio, valor);
                    if( arrayId[1].equals(tra.getCodigo())
                            && valor.equals(UtilsDominio.TRANSFERENCIA_ENVIO)) {
                        tra = transaccionRepository.getTransaccionObjetoAmbientePorOrigen(id, dominio, valor);
                    }
                } else {
                    tra = transaccionRepository.getTransaccionObjeto(id, dominio, valor);
                }

                //System.out.println("transaccion : " + tra);
                if (tra != null) {
                    tra.setLista(detalleTransaccionRepository.listaDetalle(id));
                    response.setTransaccionObjeto(tra);
                    response.setRespuesta(true);
                } else {
                    if( dominio.equals( UtilsDominio.TRANSFERENCIA ) && valor.equals(UtilsDominio.TRANSFERENCIA_ENVIO) ) {
                        tra = transaccionRepository.getTransaccionObjetoAmbientePorOrigenAll(id, dominio, valor);
                    } else if( dominio.equals( UtilsDominio.TRANSFERENCIA )) {
                        tra = transaccionRepository.getTransaccionObjetoAmbienteAll(id, dominio, valor);
                    }  else {
                        tra = transaccionRepository.getTransaccionObjetoAll(id, dominio, valor);
                    }
                    if( tra == null) {
                        response.setRespuesta(true);
                        response.setMensaje("El Nro Movimiento no existe, puede usarse");
                    }
                    else response.setMensaje("Existe pero esta dado de baja. (No se puede usar)");
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
