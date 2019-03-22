package bo.clync.pos.servicios.ambiente;

import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseList;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.common.AmbienteRepository;
import bo.clync.pos.utilitarios.UtilsDominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AmbienteServicioImpl implements AmbienteServicio {

    @Autowired
    private AmbienteRepository ambienteRepository;
    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;

    private UsuarioResponseMin obtenerAmbiente(String token, String codigo, String valor) {
        UsuarioResponseMin response = new UsuarioResponseMin();
        try {
            String codigoAmbiente = credencialRepository.getCodigoAmbienteByToken(token);
            List<UsuarioResponseMin> list = ambienteRepository.obtenerAmbiente(codigoAmbiente, codigo, valor);
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
            String codigoAmbiente = credencialRepository.getCodigoAmbienteByToken(token);
            List<UsuarioResponseMin> list = null;
            if(patron == null) {
                list = ambienteRepository.listaAmbiente(codigoAmbiente, valor);
            } else {
                list = ambienteRepository.listaAmbientePorPatron(codigoAmbiente, patron, valor);
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
