package bo.clync.pos.servicios.discos;

import bo.clync.pos.arquetipo.objetos.DiscoProcesoResponse;
import bo.clync.pos.arquetipo.objetos.DiscoResponse;
import bo.clync.pos.arquetipo.objetos.ProcesoResumen;
import bo.clync.pos.arquetipo.tablas.AbcOperaciones;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.disco.AbcOperacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DiscoServicioImpl implements DiscoServicio {

    @Autowired
    private AbcOperacionesRepository operacionesRepository;
    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;

    public DiscoProcesoResponse listaProcesos(String token) {
        DiscoProcesoResponse response = new DiscoProcesoResponse();
        try {
            ProcesoResumen resumen = null;
            List<Object[]> list = this.operacionesRepository.procesoPorToken(token);
            response.setList(new ArrayList<>());
            for (int i = 0; i < list.size() ; i++) {
                resumen = new ProcesoResumen(  (String) list.get(i)[0], (String) list.get(i)[1], (String) list.get(i)[2], (Date) list.get(i)[3]);
                response.getList().add(resumen);
            }

            response.setRespuesta(true);
        } catch (Exception e) {
            response.setRespuesta(false);
            response.setMensaje(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    public void guardarOperaciones(AbcOperaciones operaciones) {
        Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(operaciones.getToken());
        operaciones.setOperador(String.valueOf(arrayId[0]));
        operaciones.setCodigoAmbiente((String) arrayId[1]);
        this.operacionesRepository.save(operaciones);
    }

    @Override
    public DiscoResponse recuperar(String token, String process) {
        DiscoResponse response = new DiscoResponse();

        String proceso = null;
        if(process == null || process.equals("")) {
            Object[] arrayId = (Object[]) this.credencialRepository.getIdUsuarioByToken(token);
            proceso = getNombre((String) arrayId[1], (Long) arrayId[0]);
            operacionesRepository.actualizar(token, proceso, new Date());
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
    public boolean getGrabarCodigoProceso(String token, String proceso, Date fecha) {
        operacionesRepository.actualizar(token, proceso, fecha);
        return true;
    }
}
