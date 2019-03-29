package bo.clync.pos.servicios.usuario;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioRequest;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseList;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin;

import javax.servlet.http.HttpServletRequest;

public interface UsuarioServicio {

    UsuarioResponseMin obtenerUsuario(String token, String codigo, String tipoUsuario);
    ServResponse nuevoUsuario(String token, UsuarioRequest request, String tipoUsuario, HttpServletRequest http);
    UsuarioResponseList listaUsuario(String token, String patron, String tipoUsuario);

}
