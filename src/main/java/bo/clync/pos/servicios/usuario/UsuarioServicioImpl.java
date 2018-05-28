package bo.clync.pos.servicios.usuario;

import bo.clync.pos.dao.ServResponse;
import bo.clync.pos.dao.usuario.generic.UsuarioRequest;
import bo.clync.pos.dao.usuario.generic.UsuarioResponseList;
import bo.clync.pos.dao.usuario.generic.UsuarioResponseMin;
import bo.clync.pos.entity.Usuario;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.common.UsuarioRepository;
import bo.clync.pos.utilitarios.UtilsDominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(rollbackFor = Exception.class)
public class UsuarioServicioImpl implements UsuarioServicio {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;

    private UsuarioResponseMin obtenerUsuario(String token, String codigo, String tipoUsuario) {
        Usuario usuario = null;
        UsuarioResponseMin response = new UsuarioResponseMin();
        String codigoAmbiente = null;
        try {
            codigoAmbiente = credencialRepository.getCodigoAmbienteByToken(token);
            usuario = usuarioRepository.findByCodigoAndCodigoValorUsuarioAndCodigoAmbienteAndFechaBajaIsNull(codigo, tipoUsuario, codigoAmbiente);
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

    private ServResponse nuevoUsuario(String token, UsuarioRequest request, String tipoUsuario) {
        ServResponse response = new ServResponse();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if (arrayId != null) {
                Integer idUsuario = (Integer) arrayId[0];
                String codigoAmbiente = (String) arrayId[1];
                Usuario usuario = usuarioRepository.findByCodigoAndCodigoValorUsuarioAndCodigoAmbienteAndFechaBajaIsNull(request.getCodigo(), tipoUsuario, codigoAmbiente);
                if (usuario == null) {
                    usuario = new Usuario();
                    usuario.setCodigo(request.getCodigo());
                    usuario.setNombre(request.getNombre());
                    usuario.setDireccion(request.getDireccion());
                    usuario.setTelefono(request.getTelefono());
                    usuario.setCodigoAmbiente(codigoAmbiente);
                    usuario.setCodigoValorUsuario(tipoUsuario);
                    usuario.setFechaAlta(new Date());
                    usuario.setOperadorAlta(String.valueOf(idUsuario));
                    usuarioRepository.save(usuario);
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

    private UsuarioResponseList listaUsuario(String token, String patron, String tipoUsuario) {
        UsuarioResponseList response = new UsuarioResponseList();
        try {
            String codigoAmbiente = credencialRepository.getCodigoAmbienteByToken(token);
            if (patron == null)
                response.setList(usuarioRepository.getListaUsuarioResumen(tipoUsuario, codigoAmbiente));
            else
                response.setList(usuarioRepository.getListaUsuarioResumenPorPatron(patron, tipoUsuario, codigoAmbiente));
            response.setRespuesta(true);
        } catch (Exception e) {
            response.setMensaje("Error al recuperar la lista de " + tipoUsuario);
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public UsuarioResponseList listaProveedor(String token, String patron) {
        return listaUsuario(token, patron, UtilsDominio.TIPO_USUARIO_PROVEEDOR);
    }

    @Override
    public UsuarioResponseMin obtenerProveedor(String token, String codigo) {
        return obtenerUsuario(token, codigo, UtilsDominio.TIPO_USUARIO_PROVEEDOR);
    }

    @Override
    public ServResponse nuevoProveedor(String token, UsuarioRequest request) {
        return nuevoUsuario(token, request, UtilsDominio.TIPO_USUARIO_PROVEEDOR);
    }
}
