package bo.clync.pos.servicios.acceso;

import bo.clync.pos.arquetipo.dto.DatosUsuario;
import bo.clync.pos.arquetipo.objetos.acceso.AccesoRequest;
import bo.clync.pos.arquetipo.objetos.acceso.AccesoResponse;
import bo.clync.pos.repository.common.AdmCredencialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by eyave on 29-10-17.
 */
@Service
@Transactional
public class AccesoServicioImpl implements AccesoServicio {

    @Autowired
    private AdmCredencialRepository credencialRepository;
    @Override
    public AccesoResponse ingreso(AccesoRequest request) {
        AccesoResponse response = new AccesoResponse();
        try {

            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(request.getUsuario(), request.getContrasenia());

            if(datosUsuario != null && datosUsuario.getIdCredencial() != null) {

                response.setRespuesta(true);
                response.setNombreUsuario(datosUsuario.getNombreUsuario());
                response.setNombreAmbiente(datosUsuario.getNombreAmbiente());
                response.setTipo(datosUsuario.getTipoAmbiente());
                response.setToken(datosUsuario.getToken());
            } else {
                response.setMensaje("Datos Incorrectos, Intente nuevamente");
            }
        } catch (Exception e) {
            response.setMensaje("Error al generar el ingreso al sistema");
            e.printStackTrace();
        }

        return response;
    }
}
