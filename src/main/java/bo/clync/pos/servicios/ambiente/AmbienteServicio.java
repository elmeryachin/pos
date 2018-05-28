package bo.clync.pos.servicios.ambiente;

import bo.clync.pos.dao.ambiente.generic.AmbienteResponseMin;
import bo.clync.pos.dao.ambiente.generic.AmbienteResponseList;

public interface AmbienteServicio {

    AmbienteResponseMin obtenerSucursal(String token, String codigo);
    AmbienteResponseList listaSurcursal(String token, String patron);

}
