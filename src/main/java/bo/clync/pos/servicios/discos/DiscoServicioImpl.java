package bo.clync.pos.servicios.discos;

import bo.clync.pos.arquetipo.objetos.DiscoRequest;
import bo.clync.pos.arquetipo.objetos.DiscoResponse;
import bo.clync.pos.arquetipo.tablas.AbcOperaciones;
import bo.clync.pos.repository.acceso.ConectadoRepository;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.disco.AbcOperacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DiscoServicioImpl implements DiscoServicio {

    @Autowired
    private AbcOperacionesRepository operacionesRepository;
    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;

    public void guardarOperaciones(AbcOperaciones operaciones) {
        this.operacionesRepository.save(operaciones);
    }

    @Override
    public DiscoResponse recuperar(String token, String process) {
        DiscoResponse response = new DiscoResponse();

        String proceso = null;
        if(process == null || process.equals("")) {
            Object[] arrayId = (Object[]) this.credencialRepository.getIdUsuarioByToken(token);
            proceso = getNombre((String) arrayId[1], (Long) arrayId[0]);
            operacionesRepository.actualizar(token, proceso);
        } else {
            proceso = process;
        }
        response.setNombre(proceso);
        response.setList(operacionesRepository.operacionesPorProceso(proceso));
        response.setRespuesta(true);
        return response;
    }

    private String getNombre(String codigoAmbiente, Long idUsuario) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
        return sdf.format(new Date()) + "_" + codigoAmbiente + "_" + idUsuario;

    }

    @Override
    public boolean verificarProcesoExterno(String proceso) {
        List list = operacionesRepository.findAllByProceso(proceso);
        if(list != null && list.size() > 0 ) return true;
        else return false;
    }

    @Override
    public boolean getGrabarCodigoProceso(String token, String proceso) {
        operacionesRepository.actualizar(token, proceso);
        return true;
    }
}
