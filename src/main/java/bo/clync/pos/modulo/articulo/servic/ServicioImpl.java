package bo.clync.pos.modulo.articulo.servic;


import bo.clync.pos.dao.articulo.ArticuloRepository;
import bo.clync.pos.dao.articulo.DetalleArticuloPrecioRepository;
import bo.clync.pos.model.DetalleArticuloPrecio;
import bo.clync.pos.model.Articulo;
import bo.clync.pos.modulo.articulo.entity.lista.ResumenArticulo;
import bo.clync.pos.modulo.articulo.entity.lista.ServListaResponse;
import bo.clync.pos.modulo.articulo.entity.ServRequest;
import bo.clync.pos.modulo.articulo.entity.ServResponse;
import bo.clync.pos.modulo.articulo.entity.obtener.ObjetoArticulo;
import bo.clync.pos.modulo.articulo.entity.obtener.ServObtenerResponse;
import bo.clync.pos.modulo.articulo.utils.UtilsArticulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by eyave on 05-10-17.
 */
@Service
@Transactional
public class ServicioImpl implements Servicio {

    @Autowired
    private ArticuloRepository repository;

    @Autowired
    private DetalleArticuloPrecioRepository detalleRepository;

    @PersistenceContext
    private EntityManager em;


    @Override
    public ServListaResponse lista(String token) {
        Query query                 = null;
        List<ResumenArticulo> lista   = null;
        boolean respuesta           = false;
        String mensaje              = null;
        try {
            try {
                if(true) { //request!=null) {//valida token
                    //noinspection JpaQlInspection
                    lista = em
                            .createQuery
                                    ("SELECT new bo.clync.pos.modulo.articulo.entity.lista.ResumenArticulo(a.codigo, a.nombre, a.descripcion) FROM Articulo a where a.fechaBaja is null")
                            .getResultList();
                    respuesta = true;
                } else {
                    mensaje = "Se termino la session";
                }
            } catch (Exception e) {
                //Capturar error.
                e.printStackTrace();
                mensaje = "Error al recuperar los registros";
            }
            return new ServListaResponse(lista, respuesta, mensaje);
        } finally {
            query = null;
            lista = null;
            mensaje = null;
        }
    }

    @Override
    public ServObtenerResponse obtener(String codigo, String token){
        Articulo articulo = null;
        DetalleArticuloPrecio detalle = null;
        ObjetoArticulo objeto = null;
        boolean respuesta = false;
        String mensaje = null;
        try {
            try {
                if(true) { //token
                    articulo = repository.findByCodigoAndFechaBajaIsNull(codigo);
                    if(articulo != null) {
                        detalle = detalleRepository.findByIdArticuloAndFechaBajaIsNull(articulo.getId());
                        if (articulo == null || detalle == null) {
                            mensaje = "No se encontro el articulo";
                        } else {
                            objeto = new ObjetoArticulo(articulo, detalle);
                            respuesta = true;
                        }
                    } else {
                        mensaje = "No se encontro un articulo con codigo " + codigo;
                    }
                } else {
                    mensaje = "Se termino la session";
                }
            } catch (Exception e) {
                e.printStackTrace();
                mensaje = "Error al recuperar el articulo";
            }
            return new ServObtenerResponse(objeto, respuesta, mensaje);
        } finally {
            articulo = null;
            detalle =  null;
            objeto = null;
            mensaje = null;
        }
    }

    @Override
    public ServResponse nuevo(ServRequest request, String token) {
        String operador = null;
        Articulo articulo = null;
        DetalleArticuloPrecio detalle = null;
        boolean respuesta           = false;
        String mensaje              = null;
        try {
            try {
                if(true) {//token
                    operador = "EYAVE";
                    System.out.println("request : " +  request);
                    if(request!=null) {
                        System.out.println("obj : " + request.getObjetoArticulo());
                        if(request.getObjetoArticulo()!=null)
                            System.out.println(request.getObjetoArticulo().getCodigo());
                    }
                    if(repository.findByCodigo(request.getObjetoArticulo().getCodigo()) == null) {
                        articulo = UtilsArticulo.convertirNuevoArticulo(request.getObjetoArticulo(), operador);
                        repository.save(articulo);
                        detalle = UtilsArticulo.convertirNuevoDetalleArticulo(request.getObjetoArticulo(), articulo.getId(), operador);
                        detalleRepository.save(detalle);
                        respuesta = true;
                    } else {
                       mensaje = "El codigo de articulo ya existe ingreso otro.";
                    }
                } else {
                    mensaje = "Se termino la session";
                }
            } catch (Exception e) {
                e.printStackTrace();
                mensaje = "Error al guardar el registro";
            }
            return new ServResponse(respuesta, mensaje);
        } finally {
            operador = null;
            articulo = null;
            detalle = null;
            mensaje = null;
        }
    }

    @Override
    public ServResponse actualizar(String codigo,  ServRequest request, String token) {
        boolean respuesta               = false;
        String mensaje                  = null;
        Articulo articulo               = null;
        DetalleArticuloPrecio detalle   = null;
        String operador                 = null;
        DetalleArticuloPrecio detail    = null;
        try {
            try {
                if(true) { //token  // tengo que validar el request
                    operador = "AEYAVE";
                    articulo = repository.findByCodigoAndFechaBajaIsNull(codigo);
                    if(articulo != null) {
                        detalle = detalleRepository.findByIdArticuloAndFechaBajaIsNull(articulo.getId());
                        if (UtilsArticulo.convertirActualizarArticulo(articulo, request.getObjetoArticulo(), operador)) {
                            repository.save(articulo);
                            respuesta = true;
                        }
                        if (UtilsArticulo.validaActualizarDetalleArticulo(detalle, request.getObjetoArticulo())) {
                            detalle.setFechaBaja(new Date());
                            detalle.setOperador(operador);
                            detalleRepository.save(detalle);
                            detail = UtilsArticulo.convertirNuevoDetalleArticulo(request.getObjetoArticulo(),articulo.getId(), operador);
                            detalleRepository.save(detail);
                            respuesta = true;
                        }
                    } else {
                        mensaje = "No se encontro ningun articulo con el codigo enviado";
                    }
                }
                if(!respuesta) mensaje = "No se detectaron cambios para actualizar el Articulo " + request.getObjetoArticulo().getCodigo();
            } catch (Exception e) {
                e.printStackTrace();
                respuesta = false;
                mensaje = "Error al actualizar el articulo";
            }
            return new ServResponse(respuesta, mensaje);
        } finally {
            mensaje   = null;
            articulo  = null;
            detalle   = null;
            operador  = null;
            detail    = null;
        }
    }

    @Override
    public ServResponse baja(String codigo, String token) {
        boolean respuesta           = false;
        String mensaje              = null;
        Articulo articulo           = null;
        DetalleArticuloPrecio detalle = null;
        String operador             = null;
        try {
            try {
                if(true) { //token //
                    operador = "BEYAVE";
                    articulo = repository.findByCodigoAndFechaBajaIsNull(codigo);
                    if (articulo != null) {
                        detalle = detalleRepository.findByIdArticuloAndFechaBajaIsNull(articulo.getId());
                        articulo.setFechaBaja(new Date());
                        articulo.setOperador(operador);
                        articulo.setCodigo(articulo.getCodigo()+"BAJA");
                        detalle.setFechaBaja(new Date());
                        articulo.setOperador(operador);
                        repository.save(articulo);
                        detalleRepository.save(detalle);
                        respuesta = true;
                    } else {
                        mensaje = "No existe el articulo que quiere eliminar";
                    }
                } else {
                    mensaje = "Sus credenciales expiraron";
                }
            } catch (Exception e ){
                e.printStackTrace();
                mensaje = "Error en el servicio de eliminacion";
            }
            return new ServResponse(respuesta, mensaje);
        } finally {

        }
    }

}
