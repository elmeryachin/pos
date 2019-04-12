package bo.clync.pos.servicios.discos;

import bo.clync.pos.arquetipo.dto.DatosUsuario;
import bo.clync.pos.arquetipo.objetos.DiscoProcesoResponse;
import bo.clync.pos.arquetipo.objetos.DiscoResponse;
import bo.clync.pos.arquetipo.objetos.ProcesoResumen;
import bo.clync.pos.arquetipo.tablas.GesOperacion;
import bo.clync.pos.repository.common.AdmCredencialRepository;
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
    private AdmCredencialRepository credencialRepository;

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

    public void guardarOperaciones(GesOperacion operaciones) {
        DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(operaciones.getToken());
        operaciones.setOperador(datosUsuario.getIdUsuario().toString());
        operaciones.setCodigoAmbiente(datosUsuario.getCodigoAmbiente());
        operaciones.setFechaRegistro(new Date());
        this.operacionesRepository.save(operaciones);
    }

    @Override
    public DiscoResponse recuperar(String token, String nombreEnviado) {
        DiscoResponse response = new DiscoResponse();

        if(nombreEnviado.equals("")) {

            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            nombreEnviado = sdf.format(new Date()) + "_" + datosUsuario.getCodigoAmbiente() + "_" + datosUsuario.getIdUsuario();

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
    public List<GesOperacion>  getGrabarCodigoProceso(String token, String proceso, Date fecha) {

        List<GesOperacion> list = operacionesRepository.findAllByTokenAndProcesoIsNull(token);

        for (int i = 0; i < list.size(); i++) {
            GesOperacion operaciones = list.get(i);
            operaciones.setProceso(proceso);
            operaciones.setFecha(fecha);
            operacionesRepository.save(operaciones);
        }
        return list;
    }
}
