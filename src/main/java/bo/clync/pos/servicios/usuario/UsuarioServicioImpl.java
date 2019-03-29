package bo.clync.pos.servicios.usuario;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioRequest;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseList;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin;
import bo.clync.pos.arquetipo.tablas.Usuario;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.common.UsuarioRepository;
import bo.clync.pos.servicios.discos.DiscoServicio;
import bo.clync.pos.utilitarios.UtilsDisco;
import bo.clync.pos.utilitarios.UtilsDominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@Transactional(rollbackFor = Exception.class)
public class UsuarioServicioImpl implements UsuarioServicio {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;
    @Autowired
    private DiscoServicio discoServicio;

    @Override
    public UsuarioResponseMin obtenerUsuario(String token, String codigo, String tipoUsuario) {
        Usuario usuario = null;
        UsuarioResponseMin response = new UsuarioResponseMin();
        try {
            usuario = usuarioRepository.findByCodigoAndCodigoValorUsuarioAndFechaBajaIsNull(codigo, tipoUsuario);
            if (usuario == null) {
                response.setMensaje("No existe el codigo del " + tipoUsuario);
            } else {
                response.setCodigo(usuario.getCodigo());
                response.setNombre(usuario.getNombre());
                response.setRespuesta(true);
            }
        } catch (Exception e) {
            response.setMensaje("Error al recuperar el " + tipoUsuario);
            e.printStackTrace();
        }
        return response;
    }

    private static Long getId(String codigo) {
        String id = "";
        for (int i = 0; i < codigo.length(); i++) {
            char c = codigo.charAt(i);
            id = id + (int) c;
        }
        return Long.valueOf(id);
    }

    @Override
    public ServResponse nuevoUsuario(String token, UsuarioRequest request, String tipoUsuario, HttpServletRequest http) {
        ServResponse response = new ServResponse();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if (arrayId != null) {
                Long idUsuario = (Long) arrayId[0];
                String codigoAmbiente = (String) arrayId[1];
                Usuario usuario = usuarioRepository.findByCodigoAndCodigoValorUsuarioAndFechaBajaIsNull(request.getCodigo(), tipoUsuario);
                if (usuario == null) {
                    usuario = new Usuario();
                    usuario.setId(getId(request.getCodigo()));
                    usuario.setCodigo(request.getCodigo());
                    usuario.setNombre(request.getNombre());
                    usuario.setDireccion(request.getDireccion());
                    usuario.setTelefono(request.getTelefono());
                    usuario.setCodigoAmbiente(codigoAmbiente);
                    usuario.setCodigoValorUsuario(tipoUsuario);
                    usuario.setFechaAlta(new Date());
                    usuario.setOperadorAlta(String.valueOf(idUsuario));
                    usuarioRepository.save(usuario);
                    this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, request, token));
                    response.setRespuesta(true);
                } else {
                    response.setMensaje("El codigo ya existe");
                }
            } else {
                response.setMensaje("Las credenciales estan vencidas, ingrese desde el login nuevamente");
            }
        } catch (Exception e) {
            response.setMensaje("Error Al crear el nuevo usuario " + tipoUsuario);
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public UsuarioResponseList listaUsuario(String token, String patron, String tipoUsuario) {
        UsuarioResponseList response = new UsuarioResponseList();
        try {
            if (patron == null)
                response.setList(usuarioRepository.getTodosUsuarioResumen(tipoUsuario));
            else
                response.setList(usuarioRepository.getTodosUsuarioResumenPorPatron(patron, tipoUsuario));
            response.setRespuesta(true);
        } catch (Exception e) {
            response.setMensaje("Error al recuperar la lista de " + tipoUsuario);
            e.printStackTrace();
        }
        return response;
    }
}
