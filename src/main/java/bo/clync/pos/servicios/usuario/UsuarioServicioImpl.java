package bo.clync.pos.servicios.usuario;

import bo.clync.pos.arquetipo.dto.DatosUsuario;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioRequest;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseList;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin;
import bo.clync.pos.arquetipo.tablas.AdmUsuario;
import bo.clync.pos.repository.common.AdmCredencialRepository;
import bo.clync.pos.repository.common.AdmUsuarioRepository;
import bo.clync.pos.servicios.discos.DiscoServicio;
import bo.clync.pos.utilitarios.UtilsDisco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@Transactional(rollbackFor = Exception.class)
public class UsuarioServicioImpl implements UsuarioServicio {
    @Autowired
    private AdmUsuarioRepository admUsuarioRepository;
    @Autowired
    private AdmCredencialRepository credencialRepository;
    @Autowired
    private DiscoServicio discoServicio;

    @Override
    public UsuarioResponseMin obtenerUsuario(String token, String codigo, String tipoUsuario) {
        AdmUsuario admUsuario = null;
        UsuarioResponseMin response = new UsuarioResponseMin();
        try {
            admUsuario = admUsuarioRepository.findByCodigoAndCodigoValorUsuarioAndFechaBajaIsNull(codigo, tipoUsuario);
            if (admUsuario == null) {
                response.setMensaje("No existe el codigo del " + tipoUsuario);
            } else {
                response.setCodigo(admUsuario.getCodigo());
                response.setNombre(admUsuario.getNombre());
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

            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            AdmUsuario admUsuario = admUsuarioRepository.findByCodigoAndCodigoValorUsuarioAndFechaBajaIsNull(request.getCodigo(), tipoUsuario);
            if (admUsuario == null) {
                admUsuario = new AdmUsuario();
                admUsuario.setId(getId(request.getCodigo()));
                admUsuario.setCodigo(request.getCodigo());
                admUsuario.setNombre(request.getNombre());
                admUsuario.setDireccion(request.getDireccion());
                admUsuario.setTelefono(request.getTelefono());
                admUsuario.setCodigoAmbiente(datosUsuario.getCodigoAmbiente());
                admUsuario.setCodigoValorUsuario(tipoUsuario);
                admUsuario.setFechaAlta(new Date());
                admUsuario.setOperadorAlta(datosUsuario.getIdUsuario().toString());
                admUsuarioRepository.save(admUsuario);
                this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, request, token));
                response.setRespuesta(true);
            } else {
                response.setMensaje("El codigo ya existe");
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
                response.setList(admUsuarioRepository.getTodosUsuarioResumen(tipoUsuario));
            else
                response.setList(admUsuarioRepository.getTodosUsuarioResumenPorPatron(patron, tipoUsuario));
            response.setRespuesta(true);
        } catch (Exception e) {
            response.setMensaje("Error al recuperar la lista de " + tipoUsuario);
            e.printStackTrace();
        }
        return response;
    }
}
