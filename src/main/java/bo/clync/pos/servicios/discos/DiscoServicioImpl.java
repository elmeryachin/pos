package bo.clync.pos.servicios.discos;

import bo.clync.pos.arquetipo.objetos.DiscoProcesoResponse;
import bo.clync.pos.arquetipo.objetos.DiscoResponse;
import bo.clync.pos.arquetipo.objetos.ProcesoResumen;
import bo.clync.pos.arquetipo.tablas.AbcOperaciones;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.disco.AbcOperacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    private SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");

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
        operaciones.setFechaRegistro(new Date());
        this.operacionesRepository.save(operaciones);
    }

    @Override
    public DiscoResponse recuperar(String token, String nombreEnviado) {
        DiscoResponse response = new DiscoResponse();

        if(nombreEnviado.equals("")) {

            Object[] arrayId = (Object[]) this.credencialRepository.getIdUsuarioByToken(token);

            nombreEnviado = sdf.format(new Date()) + "_" + (String) arrayId[1] + "_" + (Long) arrayId[0];

            response.setList(getGrabarCodigoProceso(token, nombreEnviado, new Date()));

        } else {
            response.setList(operacionesRepository.operacionesPorProceso(nombreEnviado));
        }

        response.setNombre(nombreEnviado);
        response.setRespuesta(true);
        return response;
    }

    @Override
    public boolean verificarProcesoExterno(String proceso) {
        List list = operacionesRepository.findAllByProceso(proceso);
        if(list != null && list.size() > 0 ) return true;
        else return false;
    }

    @Override
    public List<AbcOperaciones>  getGrabarCodigoProceso(String token, String proceso, Date fecha) {
        AbcOperaciones operaciones = null;
        System.out.println("token:: " + token);
        List<AbcOperaciones> list = operacionesRepository.findAllByTokenAndProcesoIsNull(token);

        System.out.println("list::::: " + list.size());

        for (int i = 0; i < list.size(); i++) {
            operaciones = list.get(i);
            operaciones.setProceso(proceso);
            operaciones.setFecha(fecha);
            operacionesRepository.save(operaciones);
        }
        return list;
    }
}
