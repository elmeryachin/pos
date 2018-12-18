package bo.clync.pos.servicios.usuario;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioRequest;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseList;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin;

import javax.servlet.http.HttpServletRequest;

public interface UsuarioServicio {

    ///UsuarioResponseList listaProveedor(String token, String patron);
    //UsuarioResponseMin obtenerProveedor(String token, String codigo);
    //ServResponse nuevoProveedor(String token, UsuarioRequest request);

    public UsuarioResponseMin obtenerUsuario(String token, String codigo, String tipoUsuario);
    public ServResponse nuevoUsuario(String token, UsuarioRequest request, String tipoUsuario, HttpServletRequest http);
    public UsuarioResponseList listaUsuario(String token, String patron, String tipoUsuario);
    //public UsuarioResponseList listaUsuariosTodos(String token, String patron, String tipoUsuario);

}
