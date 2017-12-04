package bo.clync.pos.servicios.inventario;

import bo.clync.pos.dao.ResumenExistencia;
import bo.clync.pos.dao.inventario.ExistenciaResponseList;
import bo.clync.pos.entity.Inventario;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.common.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eyave on 03-12-17.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InventarioServicioImpl implements InventarioServicio {

    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;
    @Autowired
    private InventarioRepository inventarioRepository;

    @Override
    public ExistenciaResponseList existenciaArticulo(String token, String codigo) {
        ExistenciaResponseList response = new ExistenciaResponseList();
        if(!codigo.equals("0000")) {
            try {
                Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
                if (arrayId != null) {
                    Integer idUsuario = (Integer) arrayId[0];
                    String codigoAmbiente = (String) arrayId[1];
                    List<ResumenExistencia> lista = new ArrayList<>();
                    List<Inventario> inventarios = inventarioRepository.getInventario(codigo);
                    ResumenExistencia resumen = null;
                    Integer porLlegar = null;
                    Integer porRecibir = null;
                    for (Inventario inventario : inventarios) {
                        resumen = new ResumenExistencia();
                        resumen.setCodigoAmbiente(inventario.getCodigoAmbiente());
                        resumen.setCantidad(inventario.getExistencia());
                        if (codigoAmbiente.equals(inventario.getCodigoAmbiente())) {
                            porLlegar = inventario.getPorLlegar();
                            porRecibir = inventario.getPorRecibir();
                        }
                        lista.add(resumen);
                    }

                    resumen = new ResumenExistencia();
                    resumen.setCodigoAmbiente("Por Recibir");
                    resumen.setCantidad(porRecibir);
                    lista.add(resumen);
                    resumen = new ResumenExistencia();
                    resumen.setCodigoAmbiente("Por Llegar");
                    resumen.setCantidad(porLlegar);
                    lista.add(resumen);
                    response.setList(lista);
                    response.setRespuesta(true);
                } else {
                    response.setMensaje("Las credenciales estan vencidas, ingrese desde el login nuevamente");
                }
            } catch (Exception e) {
                response.setMensaje("Error al recuperar el registro");
                e.printStackTrace();
            }
            return response;
        } else {
            response.setRespuesta(true);
            List<ResumenExistencia> lista = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ResumenExistencia resumen = new ResumenExistencia("T" + i, (100 + i));
                lista.add(resumen);
            }
            ResumenExistencia resumen = new ResumenExistencia("Por llegar " , 10);
            lista.add(resumen);
            resumen = new ResumenExistencia("Por Recibir" , 2);
            lista.add(resumen);
            response.setList(lista);
            return response;
        }
    }
}
