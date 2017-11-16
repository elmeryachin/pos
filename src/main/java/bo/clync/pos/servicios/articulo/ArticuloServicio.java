package bo.clync.pos.servicios.articulo;

import bo.clync.pos.dao.articulo.ArticuloRequest;
import bo.clync.pos.dao.ServResponse;
import bo.clync.pos.dao.articulo.obtener.ServObtenerResponse;
import bo.clync.pos.dao.articulo.lista.ServListaResponse;

/**
 * Created by eyave on 09-10-17.
 */
public interface ArticuloServicio {

    ServListaResponse lista(String token);

    ServListaResponse listaPorCodigo(String token, String codigo);

    ServObtenerResponse obtener(String codigo, String token);

    ServResponse nuevo(ArticuloRequest request, String token);

    public ServResponse actualizar(String codigo, ArticuloRequest request, String token);

    public ServResponse eliminar(String codigo, String token);

}
