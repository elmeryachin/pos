package bo.clync.pos.modulo.articulo.servic;

import bo.clync.pos.model.Articulo;
import bo.clync.pos.modulo.articulo.entity.lista.ServListaResponse;
import bo.clync.pos.modulo.articulo.entity.ServRequest;
import bo.clync.pos.modulo.articulo.entity.ServResponse;
import bo.clync.pos.modulo.articulo.entity.obtener.ServObtenerResponse;

/**
 * Created by eyave on 09-10-17.
 */
public interface Servicio {

    ServListaResponse lista(String token);

    ServObtenerResponse obtener(String codigo, String token);

    ServResponse nuevo(ServRequest request, String token);

    public ServResponse actualizar(String codigo,  ServRequest request, String token);

    public ServResponse baja(String codigo, String token);

}
