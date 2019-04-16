package bo.clync.pos.servicios.transaccion.generic;

import bo.clync.pos.arquetipo.dto.DatosUsuario;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.*;
import bo.clync.pos.arquetipo.tablas.AdmDominio;
import bo.clync.pos.arquetipo.tablas.PosInventario;
import bo.clync.pos.arquetipo.tablas.PosTransaccion;
import bo.clync.pos.arquetipo.tablas.PosTransaccionDetalle;
import bo.clync.pos.repository.common.*;
import bo.clync.pos.repository.transaccion.pedido.TransaccionDetalleRepository;
import bo.clync.pos.repository.transaccion.pedido.TransaccionRepository;
import bo.clync.pos.utilitarios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class TransaccionServicioImpl implements TransaccionServicio {

    @Autowired
    private AdmCredencialRepository credencialRepository;
    @Autowired
    private TransaccionRepository transaccionRepository;
    @Autowired
    private AdmUsuarioRepository admUsuarioRepository;
    @Autowired
    private PosArticuloRepository posArticuloRepository;
    @Autowired
    private TransaccionDetalleRepository transaccionDetalleRepository;
    @Autowired
    private PosInventarioRepository posInventarioRepository;
    @Autowired
    private AdmCredencialRepository admCredencialRepository;
    @Autowired
    private AdmDominioRepository admDominioRepository;


    @Override
    public TransaccionResponseInit init(String token, String dominio) {
        TransaccionResponseInit init = new TransaccionResponseInit();
        try {
            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            AdmDominio admDominio = admDominioRepository.findOne(UtilsDominio.CICLO_POS);

            Integer nro = transaccionRepository.initMovimiento(datosUsuario.getIdUsuario(), datosUsuario.getCodigoAmbiente(), dominio, admDominio.getValor());
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
     * @param posTransaccion
     * @param codigoAmbiente
     */
    private void getTransaccion(String dominio, TransaccionRequest request, PosTransaccion posTransaccion, String codigoAmbiente) {
        if (dominio.equals(UtilsDominio.PEDIDO)) {

            posTransaccion.setIdUsuarioFin(admUsuarioRepository.getIdUsuarioConAmbiente(request.getTransaccionObjeto().getCodigo(), UtilsDominio.TIPO_USUARIO_PROVEEDOR, codigoAmbiente));

        } else if (dominio.equals(UtilsDominio.TRANSFERENCIA)) {

            posTransaccion.setCodigoAmbienteFin(request.getTransaccionObjeto().getCodigo());
            Long idUsuarioFin = this.admCredencialRepository.getIdUsuarioByCodigoAmbiente(request.getTransaccionObjeto().getCodigo());
            posTransaccion.setIdUsuarioFin(idUsuarioFin);

        } else if (dominio.equals(UtilsDominio.TRANSFERENCIA_NLL)) {

            posTransaccion.setCodigoAmbienteFin(request.getTransaccionObjeto().getCodigo());
            Long idUsuarioFin = this.admCredencialRepository.getIdUsuarioByCodigoAmbiente(request.getTransaccionObjeto().getCodigo());
            posTransaccion.setIdUsuarioFin(idUsuarioFin);

        } else if (dominio.equals(UtilsDominio.VENTA)) {

            posTransaccion.setIdUsuarioFin(admUsuarioRepository.getIdUsuarioCliente(request.getTransaccionObjeto().getCodigo(), UtilsDominio.TIPO_USUARIO_CLIENTE));

        } else if (dominio.equals(UtilsDominio.SOLICITUD_INTERNA)) {

            Long idUsuarioFin = this.admCredencialRepository.getIdUsuarioByCodigoAmbiente(request.getTransaccionObjeto().getCodigo());
            posTransaccion.setIdUsuarioFin(idUsuarioFin);
            posTransaccion.setCodigoAmbienteFin(request.getTransaccionObjeto().getCodigo());

        }

        posTransaccion.setObservacion(request.getTransaccionObjeto().getObservacion());
    }

    private Long getContadorDetalle(String idTransaccion) {
        Long count = transaccionDetalleRepository.getDetalleTransaccionMaximoAll(idTransaccion) + 1l;
        return count;
    }

    @Override
    public String getIdTransaccion( String dominio, String nroMovimiento, String token ) {

        AdmDominio admDominio = admDominioRepository.findOne(UtilsDominio.CICLO_POS);

        DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

        return admDominio.getValor() + "f" + datosUsuario.getCodigoAmbiente() + "f" + datosUsuario.getIdUsuario() + "f" + dominio + nroMovimiento;
    }

    @Override
    public TransaccionResponse nuevo(String token, TransaccionRequest request, String dominio, String valor, String tipoPago) throws Exception {
        TransaccionResponse response = new TransaccionResponse();
        PosTransaccion posTransaccion = null;
        PosTransaccionDetalle detalle = null;
        Date fecha = new Date();
        try {
            if (request.getTransaccionObjeto() != null) {
                if (request.getTransaccionObjeto().getLista() != null && request.getTransaccionObjeto().getLista().size() > 0) {

                    AdmDominio admDominio = admDominioRepository.findOne(UtilsDominio.CICLO_POS);

                    DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

                    //Parche para crear registro de no llegadas tras una "transferencia recibida" con edicion
                    Object[] verificacion = null;
                    if(!valor.equals(UtilsDominio.TRANSFERENCIA_NLL_RECIBIR_NO_LLEGO)) {
                        verificacion = (Object[]) this.transaccionRepository.existeNroMovimiento(datosUsuario.getIdUsuario(),
                                datosUsuario.getCodigoAmbiente(), dominio, admDominio.getValor(),
                                request.getTransaccionObjeto().getNroMovimiento());
                    }

                    if (verificacion == null || verificacion[0].equals(0l)) {
                        posTransaccion = new PosTransaccion();
                        String sid = admDominio.getValor() + "f" + datosUsuario.getCodigoAmbiente() + "f" + datosUsuario.getIdUsuario() + "f" + dominio + request.getTransaccionObjeto().getNroMovimiento();

                        //Parche para crear registro de no llegadas tras una transferencia recibida con edicion
                        if(valor.equals(UtilsDominio.TRANSFERENCIA_NLL_RECIBIR_NO_LLEGO)) {
                        	//Asignacion de id con el relacionado con sufijo "A"
                        	sid = request.getTransaccionObjeto().getId() + "A";
                        }
                        
                        posTransaccion.setId(sid);
                        posTransaccion.setCiclo(admDominio.getValor());
                        posTransaccion.setCodigoDominio(dominio);
                        posTransaccion.setCodigoValor(valor);
                        posTransaccion.setCodigoValorPago(tipoPago);
                        posTransaccion.setNroMovimiento(request.getTransaccionObjeto().getNroMovimiento());
                        posTransaccion.setIdUsuarioInicio(datosUsuario.getIdUsuario());
                        posTransaccion.setCodigoAmbienteInicio(datosUsuario.getCodigoAmbiente());
                        posTransaccion.setFechaInicio(UtilsGeneral.convertirFecha(request.getTransaccionObjeto().getFechaMovimiento()));

                        posTransaccion.setCantidad(request.getTransaccionObjeto().getCantidad());
                        posTransaccion.setPrecio(request.getTransaccionObjeto().getPrecio().add(new BigDecimal("0.00")));

                        this.getTransaccion(dominio, request, posTransaccion, datosUsuario.getCodigoAmbiente());

                        posTransaccion.setFechaAlta(fecha);
                        posTransaccion.setOperadorAlta(datosUsuario.getIdUsuario().toString());

                        posTransaccion.setFechaBaja(null);
                        posTransaccion.setOperadorBaja(null);

                        this.transaccionRepository.save(posTransaccion);

                        request.getTransaccionObjeto().setId(posTransaccion.getId());

                        Long idDetalle = getContadorDetalle(posTransaccion.getId());
                        Integer cantidadDetalle = 0 ;
                        BigDecimal precioDetalle =  new BigDecimal("0.00");

                        for (bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionDetalle transaccionDetalle : request.getTransaccionObjeto().getLista()) {

                            detalle = new PosTransaccionDetalle();
                            String uid = posTransaccion.getId() + idDetalle.toString();
                            idDetalle = idDetalle + 1L;

                            detalle.setId(uid);

                            detalle.setIdTransaccion(posTransaccion.getId());
                            detalle.setCodigoArticulo(transaccionDetalle.getCodigoArticulo());
                            detalle.setCantidad(transaccionDetalle.getCantidad());
                            detalle.setPrecio(transaccionDetalle.getPrecio());
                            detalle.setPrecioSistema(this.posArticuloRepository.getPrecioVentaPorCodigo(transaccionDetalle.getCodigoArticulo()));
                            detalle.setObservacion(transaccionDetalle.getObservacion());

                            detalle.setOperadorAlta(datosUsuario.getIdUsuario().toString());
                            detalle.setFechaAlta(fecha);

                            this.transaccionDetalleRepository.save(detalle);

                            transaccionDetalle.setId(detalle.getId());
                            cantidadDetalle = cantidadDetalle + detalle.getCantidad();
                            precioDetalle = precioDetalle.add( detalle.getPrecio().multiply(new BigDecimal(detalle.getCantidad())));

                        }

                        // Validacion de monto y cantidad entre el detalle y la cabecera principal
                        if ( !cantidadDetalle.equals(posTransaccion.getCantidad())
                                || !precioDetalle.subtract(posTransaccion.getPrecio()).toString().equals("0.00") ) {
                            throw new Exception("Cantidad y/o Montos distintos (principal) : Precio: " + posTransaccion.getPrecio() + " Cantidad: " + posTransaccion.getCantidad());
                        }

                        String msgError = getInventario(this.posInventarioRepository,
                                datosUsuario.getIdUsuario(), fecha,
                                datosUsuario.getCodigoAmbiente(),
                                posTransaccion.getCodigoAmbienteFin(),
                                dominio,
                                valor,
                                request.getTransaccionObjeto().getLista(),
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
                response.setMensaje("La posTransaccion recibida se encuentra vacia.");
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

    private String getInventario(PosInventarioRepository repository,
                                 Long idUsuario, Date fecha,
                                 String ambienteOrigen, String ambienteDestino,
                                 String dominio, String valor,
                                 List<bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionDetalle> list,
                                 Integer operador) {
        PosInventario posInventario = null;
        String msgError = null;
        for (bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionDetalle detalle : list) {
            posInventario = repository.getInventario(ambienteOrigen, detalle.getCodigoArticulo());
            if (posInventario == null) {
                posInventario = new PosInventario();
                posInventario.setCodigoAmbiente(ambienteOrigen);
                posInventario.setCodigoArticulo(detalle.getCodigoArticulo());
                posInventario.setExistencia(0);
                posInventario.setPorLlegar(detalle.getCantidad());
                posInventario.setFechaAlta(fecha);
                posInventario.setOperadorAlta(String.valueOf(idUsuario));
            }
            if (dominio.equals(UtilsDominio.PEDIDO) && valor.equals(UtilsDominio.PEDIDO_SOLICITUD)) {

                posInventario.setPorLlegar(UtilsOperacion.getNumeroNoNulo(posInventario.getPorLlegar()) + operador * detalle.getCantidad());

                repository.save(posInventario);

            } else if (dominio.equals(UtilsDominio.TRANSFERENCIA) && valor.equals(UtilsDominio.TRANSFERENCIA_ENVIO)) {
                posInventario.setPorEntregar(UtilsOperacion.getNumeroNoNulo(posInventario.getPorEntregar()) + operador * detalle.getCantidad());
                posInventario.setExistencia(posInventario.getExistencia() - operador * detalle.getCantidad());
                repository.save(posInventario);

                posInventario = repository.getInventario(ambienteDestino, detalle.getCodigoArticulo());
                if (posInventario == null) {
                    posInventario = new PosInventario();
                    posInventario.setCodigoAmbiente(ambienteDestino);
                    posInventario.setCodigoArticulo(detalle.getCodigoArticulo());
                    posInventario.setExistencia(0);
                    posInventario.setFechaAlta(fecha);
                    posInventario.setOperadorAlta(String.valueOf(idUsuario));
                }
                posInventario.setPorRecibir(UtilsOperacion.getNumeroNoNulo(posInventario.getPorRecibir()) + operador * detalle.getCantidad());
                repository.save(posInventario);
            } else if (dominio.equals(UtilsDominio.TRANSFERENCIA_NLL) && valor.equals(UtilsDominio.TRANSFERENCIA_NLL_RECIBIR_NO_LLEGO)) {
            	// NOT IMPLEMENTS
            	posInventario.setExistencia( posInventario.getExistencia() - operador * detalle.getCantidad() );
            	repository.save(posInventario);
            	
            	posInventario = repository.getInventario( ambienteDestino, detalle.getCodigoArticulo() );
            	posInventario.setExistencia( posInventario.getExistencia() + operador * detalle.getCantidad() );
            	repository.save(posInventario);
            	
            } else if (dominio.equals(UtilsDominio.VENTA) && valor.equals(UtilsDominio.VENTA_REALIZADA)) {
                posInventario.setExistencia(posInventario.getExistencia() - operador * detalle.getCantidad());
                repository.save(posInventario);
            }
        }

        return msgError;
    }

    @Override
    public TransaccionResponse actualizar(String token, TransaccionRequest request, String dominio) throws Exception {
        TransaccionResponse response = new TransaccionResponse();
        PosTransaccion posTransaccion = null;
        PosTransaccionDetalle detalle = null;
        Date fecha = new Date();
        try {

            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            posTransaccion = this.transaccionRepository.findOne(request.getTransaccionObjeto().getId());

            if (posTransaccion.getFechaBaja() == null) {
                List<bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionDetalle> listaDetalle = this.transaccionDetalleRepository.listaDetalle(request.getTransaccionObjeto().getId());

                this.getInventario(this.posInventarioRepository,
                        datosUsuario.getIdUsuario(),
                        fecha,
                        datosUsuario.getCodigoAmbiente(),
                        posTransaccion.getCodigoAmbienteFin(),
                        posTransaccion.getCodigoDominio(),
                        posTransaccion.getCodigoValor(),
                        listaDetalle,
                        UtilsConstante.INVENTARIO_REMOVE);

                if (posTransaccion != null) {

                    this.getTransaccion(dominio, request, posTransaccion, datosUsuario.getCodigoAmbiente());

                    posTransaccion.setFechaActualizacion(fecha);
                    posTransaccion.setOperadorActualizacion(datosUsuario.getIdUsuario().toString());

                    posTransaccion.setPrecio(request.getTransaccionObjeto().getPrecio().add(new BigDecimal("0.00")));
                    posTransaccion.setCantidad(request.getTransaccionObjeto().getCantidad());

                    this.transaccionRepository.save(posTransaccion);
                    request.getTransaccionObjeto().setId(posTransaccion.getId());

                    BigDecimal precioDetalle = new BigDecimal(("0.00"));
                    Integer cantidadDetalle = 0;
                    List<String> listaActualizada = new ArrayList<>();
                    for (bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionDetalle de : request.getTransaccionObjeto().getLista()) {
                        if (de.getId() != null) {
                            listaActualizada.add(de.getId());
                            detalle = this.transaccionDetalleRepository.findOne(de.getId());
                            detalle.setCodigoArticulo(de.getCodigoArticulo());
                            detalle.setCantidad(de.getCantidad());
                            detalle.setPrecio(de.getPrecio());
                            detalle.setObservacion(de.getObservacion());
                            detalle.setOperadorActualizacion(datosUsuario.getIdUsuario().toString());
                            detalle.setFechaActualizacion(fecha);
                            this.transaccionDetalleRepository.save(detalle);

                            precioDetalle = precioDetalle.add(detalle.getPrecio().multiply(new BigDecimal(detalle.getCantidad())));
                            cantidadDetalle = cantidadDetalle + detalle.getCantidad();
                        }
                    }

                    List<PosTransaccionDetalle> listBaja = this.transaccionDetalleRepository.findByIdTransaccionAndIdNotInAndFechaBajaIsNull( posTransaccion.getId(), listaActualizada);

                    System.out.println("listaBaja: " + listBaja.size());

                    for (PosTransaccionDetalle de : listBaja) {
                        de.setFechaBaja(fecha);
                        de.setOperadorBaja(datosUsuario.getIdUsuario().toString());
                        this.transaccionDetalleRepository.save(de);
                    }

                    for (bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionDetalle de : request.getTransaccionObjeto().getLista()) {
                        if (de.getId() == null) {
                            String idDetalleExistente = this.transaccionDetalleRepository.existeArticuloEnTransaccion(posTransaccion.getId(), de.getCodigoArticulo());
                            if (idDetalleExistente == null) {
                                detalle = new PosTransaccionDetalle();
                                detalle.setId(posTransaccion.getId() + getContadorDetalle(posTransaccion.getId()).toString());

                                detalle.setIdTransaccion(posTransaccion.getId());
                                detalle.setCodigoArticulo(de.getCodigoArticulo());
                                detalle.setCantidad(de.getCantidad());
                                detalle.setPrecio(de.getPrecio());
                                detalle.setPrecioSistema(this.posArticuloRepository.getPrecioVentaPorCodigo(de.getCodigoArticulo()));
                                detalle.setObservacion(de.getObservacion());

                                detalle.setOperadorAlta(datosUsuario.getIdUsuario().toString());
                                detalle.setFechaAlta(fecha);

                                transaccionDetalleRepository.save(detalle);

                                precioDetalle = precioDetalle.add(detalle.getPrecio().multiply(new BigDecimal(detalle.getCantidad())));
                                cantidadDetalle = cantidadDetalle + detalle.getCantidad();

                                de.setId(detalle.getId());
                                listaActualizada.add(de.getId());
                            } else {
                                response.setMensaje("Esta volviendo a insertar un articulo");
                            }
                        }
                    }

                    if ( !cantidadDetalle.equals(posTransaccion.getCantidad())
                            || !precioDetalle.subtract(posTransaccion.getPrecio()).toString().equals("0.00")) {
                        throw new Exception("Cantidad y/o Montos distintos (principal) : Precio: " + posTransaccion.getPrecio() + " Cantidad: " + posTransaccion.getCantidad());
                    }

                    String msgError = this.getInventario(this.posInventarioRepository,
                            datosUsuario.getIdUsuario(),
                            fecha,
                            datosUsuario.getCodigoAmbiente(),
                            posTransaccion.getCodigoAmbienteFin(),
                            posTransaccion.getCodigoDominio(),
                            posTransaccion.getCodigoValor(),
                            request.getTransaccionObjeto().getLista(),
                            UtilsConstante.INVENTARIO_ADD);

                    response.setMensaje(msgError);
                    if (response.getMensaje() == null) {
                        response.setTransaccionObjeto(request.getTransaccionObjeto());
                        response.setRespuesta(true);
                    }
                } else {
                    response.setMensaje("No se encontro la posTransaccion");
                }
            } else {
                response.setMensaje("PosTransaccion no editable   (Se encuentra dada de baja)");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setTransaccionObjeto(null);
            response.setMensaje("Error al actualizar la posTransaccion ");
        }
        if (!response.isRespuesta())
            throw new Exception(response.getMensaje());
        return response;
    }

    @Override
    public ServResponse eliminar(String token, String idTransaccion, String valor) throws Exception {
        ServResponse response = new ServResponse();
        try {

            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            Date fecha = new Date();
            PosTransaccion posTransaccion = this.transaccionRepository.findByIdAndFechaBajaIsNull(idTransaccion);

            if (posTransaccion.getCodigoValor().equals(valor)) {

                posTransaccion.setFechaBaja(fecha);
                posTransaccion.setOperadorBaja(datosUsuario.getIdUsuario().toString());
                this.transaccionRepository.save(posTransaccion);
                List<PosTransaccionDetalle> detalles = this.transaccionDetalleRepository.findByIdTransaccionAndFechaBajaIsNull(posTransaccion.getId());
                List<bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionDetalle> listAux = new ArrayList<>();

                for (PosTransaccionDetalle detalle : detalles) {

                    bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionDetalle aux = new bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionDetalle();
                    detalle.setFechaBaja(fecha);
                    detalle.setOperadorBaja(datosUsuario.getIdUsuario().toString());
                    this.transaccionDetalleRepository.save(detalle);
                    aux.setCantidad(detalle.getCantidad());
                    aux.setCodigoArticulo(detalle.getCodigoArticulo());
                    listAux.add(aux);

                }
                this.getInventario(this.posInventarioRepository,
                        datosUsuario.getIdUsuario(),
                        fecha,
                        datosUsuario.getCodigoAmbiente(),
                        posTransaccion.getCodigoAmbienteFin(),
                        posTransaccion.getCodigoDominio(),
                        posTransaccion.getCodigoValor(),
                        listAux,
                        UtilsConstante.INVENTARIO_REMOVE);

                response.setRespuesta(true);
            } else {
                response.setMensaje("No se puede dar de baja la posTransaccion , los permitidos {PEDIDO SOLICITUD, TRANSFERENCIA ENVIADO}");
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
        List<TransaccionObjeto> transaccionesSinDetalle = new ArrayList();
        try {
            AdmDominio admDominio = admDominioRepository.findOne(UtilsDominio.CICLO_POS);

            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            List<TransaccionObjeto> lista = null;

            if (dominio.equals(UtilsDominio.PEDIDO)) {
                lista = this.transaccionRepository.listaPedidos(datosUsuario.getCodigoAmbiente(), dominio, valor, admDominio.getValor(), tipoUsuario);
            } else if (dominio.equals(UtilsDominio.TRANSFERENCIA)
                    && valor.equals(UtilsDominio.TRANSFERENCIA_ENVIO)) {
                lista = this.transaccionRepository.listaTransferenciasEnviosPorOrigen(datosUsuario.getCodigoAmbiente(), dominio, valor, admDominio.getValor());
            } else if (dominio.equals(UtilsDominio.TRANSFERENCIA)
                    && valor.equals(UtilsDominio.TRANSFERENCIA_ENVIO_DESTINO_AUX)) {
                lista = this.transaccionRepository.listaTransferenciasEnviosPorDestino(datosUsuario.getCodigoAmbiente(), dominio, UtilsDominio.TRANSFERENCIA_ENVIO, admDominio.getValor());
            } else if (dominio.equals(UtilsDominio.TRANSFERENCIA)
                    && valor.equals(UtilsDominio.TRANSFERENCIA_RECIBIR)) {
                lista = this.transaccionRepository.listaTransferenciasRecibidos(datosUsuario.getCodigoAmbiente(), dominio, valor, UtilsDominio.TRANSFERENCIA_RECIBIR_EDIT, admDominio.getValor());
            }  else if (dominio.equals(UtilsDominio.TRANSFERENCIA)
                    && valor.equals(UtilsDominio.TRANSFERENCIA_RECIBIR_CONF)) {
                lista = this.transaccionRepository.listaTransferenciasEnviosPorOrigen(datosUsuario.getCodigoAmbiente(), dominio, valor, admDominio.getValor());
            }  else if (dominio.equals(UtilsDominio.TRANSFERENCIA)
                    && valor.equals(UtilsDominio.TRANSFERENCIA_RECIBIR_ORIGEN_AUX)) {
                lista = this.transaccionRepository.listaTransferenciasRecibidosPorOrigen(datosUsuario.getCodigoAmbiente(), dominio, UtilsDominio.TRANSFERENCIA_RECIBIR, UtilsDominio.TRANSFERENCIA_RECIBIR_EDIT, admDominio.getValor());
            } else if (dominio.equals(UtilsDominio.VENTA)
                    && valor.equals(UtilsDominio.VENTA_REALIZADA)) {
                lista = this.transaccionRepository.listaVentas(datosUsuario.getCodigoAmbiente(), dominio, valor, admDominio.getValor(), tipoUsuario);
            } else if (dominio.equals(UtilsDominio.VENTA)
                    && valor.equals(UtilsDominio.VENTA_CONFIRMADA)) {
                lista = this.transaccionRepository.listaVentas(datosUsuario.getCodigoAmbiente(), dominio, valor, admDominio.getValor(), tipoUsuario);
            } else if (dominio.equals(UtilsDominio.SOLICITUD_INTERNA)) {
                lista = this.transaccionRepository.listaTransferenciasEnviosPorOrigen(datosUsuario.getCodigoAmbiente(), dominio, valor, admDominio.getValor());
            } else if (dominio.equals(UtilsDominio.SOLICITUD_INTERNA_AUX)) {
                lista = this.transaccionRepository.listaTransferenciasEnviosTodos(dominio, valor, admDominio.getValor());
            }

            for (TransaccionObjeto transaccion : lista) {

                transaccion.setLista(this.transaccionDetalleRepository.listaDetalle(transaccion.getId()));

                if (transaccion.getLista() == null || transaccion.getLista().size() == 0) {
                    transaccionesSinDetalle.add(transaccion);
                }
            }
            lista.removeAll(transaccionesSinDetalle);
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

            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            TransaccionObjeto tra = null;
            if( dominio.equals( UtilsDominio.TRANSFERENCIA ) || dominio.equals( UtilsDominio.SOLICITUD_INTERNA )) {

                tra = transaccionRepository.getTransaccionObjetoAmbiente(id, dominio, valor);

                if( datosUsuario.getCodigoAmbiente().equals(tra.getCodigo()) && valor.equals(UtilsDominio.TRANSFERENCIA_ENVIO) ) {
                    tra = transaccionRepository.getTransaccionObjetoAmbientePorOrigen(id, dominio, valor);
                }

            } else {
                tra = transaccionRepository.getTransaccionObjeto(id, dominio, valor);
            }

            if (tra != null) {
                tra.setLista(transaccionDetalleRepository.listaDetalle(id));
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
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al obtener el pedido");
        }
        return response;
    }

}
