package bo.clync.pos.servicios.articulo;


import bo.clync.pos.entity.Articulo;
import bo.clync.pos.model.articulo.ArticuloRequest;
import bo.clync.pos.model.ServResponse;
import bo.clync.pos.model.articulo.lista.ResumenArticulo;
import bo.clync.pos.model.articulo.lista.ServListaResponse;
import bo.clync.pos.model.articulo.obtener.ObjetoArticulo;
import bo.clync.pos.model.articulo.obtener.ServObtenerResponse;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
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
public class ArticuloServicioImpl implements ArticuloServicio {

    @Autowired
    private ArticuloRepository repository;
    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;

    @Override
    public ServListaResponse lista(String token) {
        ServListaResponse response = new ServListaResponse();
        try {
            response.setLista(repository.getListResumenArticulos());
            response.setRespuesta(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al recuperar los registros");
        }
        return response;
    }

    @Override
    public ServListaResponse listaPorCodigo(String token, String codigo) {
        ServListaResponse response = new ServListaResponse();
        try {
            response.setLista(repository.getListResumenArticulosPorPatron(codigo));
            response.setRespuesta(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al recuperar los registros");
        }
        return response;
    }

    @Override
    public ServObtenerResponse obtener(String codigo, String token){
        ServObtenerResponse response = new ServObtenerResponse();
        try {
            response.setArticulo(repository.getObtenerArticuloPorCodigo(codigo));
            if(response.getArticulo()==null)
                response.setMensaje("No se encontro un articulo con codigo " + codigo);
            else response.setRespuesta(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al recuperar el articulo");
        }
        return response;
    }

    @Override
    public ServResponse nuevo(ArticuloRequest request, String token) {
        String operador         = null;
        Articulo articulo       = null;
        ServResponse response   = new ServResponse();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            operador = String.valueOf(arrayId[0]);
            if(repository.getObtenerArticuloPorCodigo(request.getObjetoArticulo().getCodigo()) == null) {
                articulo = new Articulo();
                articulo.setCodigo(request.getObjetoArticulo().getCodigo());
                articulo.setNombre(request.getObjetoArticulo().getNombre());
                articulo.setDescripcion(request.getObjetoArticulo().getDescripcion());
                articulo.setPrecioKilo(request.getObjetoArticulo().getPrecioKilo());
                articulo.setPeso(request.getObjetoArticulo().getPeso());
                articulo.setPrecioZonaLibre(request.getObjetoArticulo().getPrecioZonaLibre());
                articulo.setPorcentajeGasto(request.getObjetoArticulo().getPorcentajeGasto());
                articulo.setPrecioCompra(request.getObjetoArticulo().getPrecioCompra());
                articulo.setPrecioVenta(request.getObjetoArticulo().getPrecioVenta());
                articulo.setPrecioMercado(request.getObjetoArticulo().getPrecioMercado());
                articulo.setFechaAlta(new Date());
                articulo.setOperadorAlta(operador);
                repository.save(articulo);
                response.setRespuesta(true);
            } else {
                response.setMensaje("El codigo de articulo ya existe ingreso otro.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al guardar el registro");
        }
        return response;
    }

    @Override
    public ServResponse actualizar(String codigo, ArticuloRequest request, String token) {
        Articulo articulo               = null;
        String operador                 = null;
        ServResponse response = new ServResponse();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            operador = String.valueOf(arrayId[0]);
            articulo = repository.findByCodigoAndFechaBajaIsNull(codigo);
            if(articulo != null) {
                articulo.setNombre(request.getObjetoArticulo().getNombre());
                articulo.setDescripcion(request.getObjetoArticulo().getDescripcion());
                articulo.setPrecioKilo(request.getObjetoArticulo().getPrecioKilo());
                articulo.setPeso(request.getObjetoArticulo().getPeso());
                articulo.setPrecioZonaLibre(request.getObjetoArticulo().getPrecioZonaLibre());
                articulo.setPorcentajeGasto(request.getObjetoArticulo().getPorcentajeGasto());
                articulo.setPrecioCompra(request.getObjetoArticulo().getPrecioCompra());
                articulo.setPrecioVenta(request.getObjetoArticulo().getPrecioVenta());
                articulo.setPrecioMercado(request.getObjetoArticulo().getPrecioMercado());
                articulo.setFechaActualizacion(new Date());
                articulo.setOperadorActualizacion(operador);
                repository.save(articulo);
                response.setRespuesta(true);
            } else {
                response.setMensaje("No se encontro ningun articulo con el codigo enviado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al actualizar el articulo");
        }
        return response;
    }

    @Override
    public ServResponse eliminar(String codigo, String token) {
        Articulo articulo       = null;
        String operador         = null;
        Object[] arrayId        = null;
        ServResponse response   = new ServResponse();
        try {
            arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if(arrayId != null) {
                operador = String.valueOf(arrayId[0]);
                articulo = repository.findByCodigoAndFechaBajaIsNull(codigo);
                if (articulo != null) {
                    articulo.setFechaBaja(new Date());
                    articulo.setOperadorBaja(operador);
                    repository.save(articulo);
                    response.setRespuesta(true);
                } else {
                    response.setMensaje("No existe el articulo que quiere eliminar");
                }
            } else {
                response.setMensaje("Su session expiro");
            }
        } catch (Exception e ){
            e.printStackTrace();
            response.setMensaje("Error en el servicio de eliminacion");
        }
        return response;
    }

}
