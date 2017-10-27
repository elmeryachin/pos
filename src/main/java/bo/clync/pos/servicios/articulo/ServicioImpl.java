package bo.clync.pos.servicios.articulo;


import bo.clync.pos.entity.Articulo;
import bo.clync.pos.model.articulo.ServRequest;
import bo.clync.pos.model.articulo.ServResponse;
import bo.clync.pos.model.articulo.lista.ResumenArticulo;
import bo.clync.pos.model.articulo.lista.ServListaResponse;
import bo.clync.pos.model.articulo.obtener.ObjetoArticulo;
import bo.clync.pos.model.articulo.obtener.ServObtenerResponse;
import bo.clync.pos.repository.articulo.ArticuloRepository;
import bo.clync.pos.utilitarios.articulo.UtilsArticulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager em;


    @Override
    public ServListaResponse lista(String token) {
        List<ResumenArticulo> lista   = null;
        boolean respuesta             = false;
        String mensaje                = null;
        try {
            lista = repository.getListResumenArticulos();
            respuesta = true;
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "Error al recuperar los registros";
        }
        return new ServListaResponse(lista, respuesta, mensaje);
    }

    @Override
    public ServObtenerResponse obtener(String codigo, String token){
        ObjetoArticulo objeto = null;
        boolean respuesta = false;
        String mensaje = null;
        try {
            objeto = repository.getObtenerArticuloPorCodigo(codigo);
            if(objeto==null)
                mensaje = "No se encontro un articulo con codigo " + codigo;
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "Error al recuperar el articulo";
        }
        return new ServObtenerResponse(objeto, respuesta, mensaje);
    }

    @Override
    public ServResponse nuevo(ServRequest request, String token) {
        String operador     = null;
        Articulo articulo   = null;
        boolean respuesta   = false;
        String mensaje      = null;
        try {
            operador = "EYAVE";
            if(repository.getObtenerArticuloPorCodigo(request.getObjetoArticulo().getCodigo()) == null) {
                articulo = UtilsArticulo.convertirNuevoArticulo(request.getObjetoArticulo(), operador);
                repository.save(articulo);
                respuesta = true;
            } else {
                mensaje = "El codigo de articulo ya existe ingreso otro.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "Error al guardar el registro";
        }
        return new ServResponse(respuesta, mensaje);
    }

    @Override
    public ServResponse actualizar(String codigo,  ServRequest request, String token) {
        boolean respuesta               = false;
        String mensaje                  = null;
        Articulo articulo               = null;
        String operador                 = null;
        try {
            operador = "AEYAVE";
            articulo = repository.findByCodigoAndFechaBajaIsNull(codigo);
            if(articulo != null) {
                if (UtilsArticulo.convertirActualizarArticulo(articulo, request.getObjetoArticulo(), operador)) {
                    repository.save(articulo);
                    respuesta = true;
                }
            } else {
                mensaje = "No se encontro ningun articulo con el codigo enviado";
            }
            if(!respuesta) mensaje = "No se detectaron cambios para actualizar el Articulo " + request.getObjetoArticulo().getCodigo();
        } catch (Exception e) {
            e.printStackTrace();
            respuesta = false;
            mensaje = "Error al actualizar el articulo";
        }
        return new ServResponse(respuesta, mensaje);
    }

    @Override
    public ServResponse baja(String codigo, String token) {
        boolean respuesta           = false;
        String mensaje              = null;
        Articulo articulo           = null;
        String operador             = null;
        try {
             operador = "BEYAVE";
             articulo = repository.findByCodigoAndFechaBajaIsNull(codigo);
             if (articulo != null) {
                articulo.setFechaBaja(new Date());
                articulo.setOperadorBaja(operador);
                articulo.setCodigo("_"+articulo.getCodigo());
                repository.save(articulo);
                    respuesta = true;
             } else {
                mensaje = "No existe el articulo que quiere eliminar";
             }
        } catch (Exception e ){
            e.printStackTrace();
            mensaje = "Error en el servicio de eliminacion";
        }
        return new ServResponse(respuesta, mensaje);
    }

}
