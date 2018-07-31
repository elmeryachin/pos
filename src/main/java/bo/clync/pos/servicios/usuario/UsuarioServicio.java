package bo.clync.pos.servicios.usuario;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioRequest;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseList;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin;

public interface UsuarioServicio {

    UsuarioResponseList listaProveedor(String token, String patron);
    UsuarioResponseMin obtenerProveedor(String token, String codigo);
    ServResponse nuevoProveedor(String token, UsuarioRequest request);


}
