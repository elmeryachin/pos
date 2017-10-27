package bo.clync.pos.servicios.articulo;

import bo.clync.pos.model.articulo.ServRequest;
import bo.clync.pos.model.articulo.ServResponse;
import bo.clync.pos.model.articulo.obtener.ServObtenerResponse;
import bo.clync.pos.model.articulo.lista.ServListaResponse;

/**
 * Created by eyave on 09-10-17.
 */
public interface Servicio {

    ServListaResponse lista(String token);

    ServObtenerResponse obtener(String codigo, String token);

    ServResponse nuevo(ServRequest request, String token);

    public ServResponse actualizar(String codigo, ServRequest request, String token);

    public ServResponse baja(String codigo, String token);

}
