package bo.clync.pos.servicios.ambiente;

import bo.clync.pos.arquetipo.objetos.generic.AmbienteResponseMin;
import bo.clync.pos.arquetipo.objetos.generic.AmbienteResponseList;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseList;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin;

public interface AmbienteServicio {

    UsuarioResponseMin obtenerSucursal(String token, String codigo);
    UsuarioResponseList listaSurcursal(String token, String patron);

}
