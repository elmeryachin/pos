package bo.clync.pos.servicios.acceso;

import bo.clync.pos.arquetipo.tablas.Conectado;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.acceso.AccesoRequest;
import bo.clync.pos.arquetipo.objetos.acceso.AccesoResponse;
import bo.clync.pos.repository.acceso.ConectadoRepository;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.common.CicloRepository;
import bo.clync.pos.utilitarios.acceso.UtilsConectado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by eyave on 29-10-17.
 */
@Service
@Transactional
public class AccesoServicioImpl implements AccesoServicio {

    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;
    @Autowired
    private ConectadoRepository conectadoRepository;
    @Autowired
    private CicloRepository cicloRepository;
    @Override
    public AccesoResponse ingreso(AccesoRequest request) {
        AccesoResponse response = new AccesoResponse();
        try {
            Object[] uac = (Object[]) credencialRepository.getAutenticacion(request.getUsuario());
            if(uac[0]!=null && uac[0].toString().equals(request.getContrasenia())) {
                String tokn = UtilsConectado.generarToken();
                Date fechaI = cicloRepository.getFechaInicio();
                if(conectadoRepository.buscarToken(request.getUsuario()) == null) {
                    Conectado c = new Conectado((Integer) uac[1], tokn, new java.util.Date());
                    conectadoRepository.save(c);
                    response.setToken(tokn);
                } else {
                    response.setToken(conectadoRepository.buscarToken(request.getUsuario()));
                }
                response.setRespuesta(true);
                response.setFechaInicioCiclo(fechaI);
                response.setNombreUsuario((String) uac[2]);
                response.setNombreAmbiente((String) uac[3]);
                response.setTipo((String) uac[4]);
            } else {
                response.setMensaje("Los datos de autenticacion son incorrectos");
            }
        } catch (Exception e) {
            response.setMensaje("Error al generar el ingreso al sistema");
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public ServResponse salir(String token) {
        ServResponse response = new ServResponse();
        try {
            Conectado conectado = conectadoRepository.findByTokenAndFechaFinIsNull(token);
            conectado.setFechaFin(new Date());
            conectadoRepository.save(conectado);
            response.setRespuesta(true);
        } catch (Exception e) {
            response.setMensaje("Error al cerrar la session");
            e.printStackTrace();
        }
        return response;
    }
}
