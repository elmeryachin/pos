package bo.clync.pos.servicios.usuario;

import bo.clync.pos.dao.ServResponse;
import bo.clync.pos.dao.usuario.generic.UsuarioRequest;
import bo.clync.pos.dao.usuario.generic.UsuarioResponseList;
import bo.clync.pos.dao.usuario.generic.UsuarioResponseMin;

public interface UsuarioServicio {

    UsuarioResponseList listaProveedor(String token, String patron);
    UsuarioResponseMin obtenerProveedor(String token, String codigo);
    ServResponse nuevoProveedor(String token, UsuarioRequest request);


}
