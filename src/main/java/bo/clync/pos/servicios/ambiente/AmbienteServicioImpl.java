package bo.clync.pos.servicios.ambiente;

import bo.clync.pos.arquetipo.dto.DatosUsuario;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseList;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin;
import bo.clync.pos.repository.common.AdmCredencialRepository;
import bo.clync.pos.repository.common.PosAmbienteRepository;
import bo.clync.pos.utilitarios.UtilsDominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AmbienteServicioImpl implements AmbienteServicio {

    @Autowired
    private PosAmbienteRepository posAmbienteRepository;
    @Autowired
    private AdmCredencialRepository credencialRepository;

    private UsuarioResponseMin obtenerAmbiente(String token, String codigo, String valor) {
        UsuarioResponseMin response = new UsuarioResponseMin();
        try {
            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);
            List<UsuarioResponseMin> list = posAmbienteRepository.obtenerAmbiente(datosUsuario.getCodigoAmbiente(), codigo, valor);
            if(list != null && list.size() > 0) {
                response = list.get(0);
                response.setRespuesta(true);
            }
        } catch (Exception e) {
            response.setMensaje("No existe o es con el que esta conectado : " + codigo);
        }
        return response;
    }

    private UsuarioResponseList listaAmbiente(String token, String patron, String valor) {
        UsuarioResponseList response = new UsuarioResponseList();
        try {
            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);
            List<UsuarioResponseMin> list = null;
            if(patron == null) {
                list = posAmbienteRepository.listaAmbiente(datosUsuario.getCodigoAmbiente(), valor);
            } else {
                list = posAmbienteRepository.listaAmbientePorPatron(datosUsuario.getCodigoAmbiente(), patron, valor);
            }
            response.setList(list);
            response.setRespuesta(true);
        } catch (Exception e) {
            response.setMensaje("Error al recuperar la lista de ambientes");
            e.printStackTrace();
        }
        return response;
    }


    @Override
    public UsuarioResponseMin obtenerSucursal(String token, String codigo) {
        return obtenerAmbiente(token, codigo, UtilsDominio.TIPO_AMBIENTE_SUCURSAL);
    }

    @Override
    public UsuarioResponseList listaSurcursal(String token, String patron) {
        return listaAmbiente(token, patron, UtilsDominio.TIPO_AMBIENTE_SUCURSAL);
    }
}
