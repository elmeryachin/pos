package bo.clync.pos.servicios.ambiente;

import bo.clync.pos.arquetipo.objetos.generic.AmbienteResponseMin;
import bo.clync.pos.arquetipo.objetos.generic.AmbienteResponseList;

public interface AmbienteServicio {

    AmbienteResponseMin obtenerSucursal(String token, String codigo);
    AmbienteResponseList listaSurcursal(String token, String patron);

}
