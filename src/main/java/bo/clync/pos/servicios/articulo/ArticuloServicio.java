package bo.clync.pos.servicios.articulo;

import bo.clync.pos.dao.articulo.ArticuloRequest;
import bo.clync.pos.dao.ServResponse;
import bo.clync.pos.dao.articulo.obtener.ServObtenerResponse;
import bo.clync.pos.dao.articulo.lista.ServListaResponse;
import bo.clync.pos.dao.articulo.ArticuloResponseList;
import bo.clync.pos.dao.articulo.ArticuloResponseMin;

/**
 * Created by eyave on 09-10-17.
 */
public interface ArticuloServicio {

    ServListaResponse lista(String token);

    ServListaResponse listaNativa(String token);

    ServListaResponse listaPorCodigo(String token, String patron);

    ServObtenerResponse obtener(String codigo, String token);

    ServResponse nuevo(ArticuloRequest request, String token);

    public ServResponse actualizar(String codigo, ArticuloRequest request, String token);

    public ServResponse eliminar(String codigo, String token);

    public byte[] reporteListaArticulos(String token, String tipo);

    ArticuloResponseMin obtenerArticulo(String token, String codigo);

    ArticuloResponseList listaArticulo(String token, String patron);
}
