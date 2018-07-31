package bo.clync.pos.servicios.inventario;

import bo.clync.pos.arquetipo.objetos.ResumenExistencia;
import bo.clync.pos.arquetipo.objetos.inventario.ExistenciaResponseList;
import bo.clync.pos.arquetipo.objetos.inventario.Sucursales;
import bo.clync.pos.arquetipo.objetos.inventario.SucursalesResponseList;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.common.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    @PersistenceContext
    private EntityManager em;

    @Override
    public ExistenciaResponseList existenciaArticulo(String token, String codigo) {
        ExistenciaResponseList response = new ExistenciaResponseList();
            try {
                Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
                if (arrayId != null) {
                    Integer idUsuario = (Integer) arrayId[0];
                    String codigoAmbiente = (String) arrayId[1];
                    List<ResumenExistencia> lista = new ArrayList<>();
                    String sql = "   SELECT a.nombre, a.codigo, i.existencia, i.por_llegar, i.por_entregar, i.por_recibir " +
                                 "     FROM ambiente a LEFT OUTER JOIN ( " +
                                 "                                  SELECT codigo_ambiente, existencia, por_llegar, por_entregar, por_recibir " +
                                 "                                    FROM inventario " +
                                 "                                   WHERE codigo_articulo = ?1 " +
                                 "                                     AND fecha_baja IS NULL " +
                                 "                                      ) i " +
                                 "       ON a.codigo = i.codigo_ambiente " +
                                 "    WHERE a.fecha_baja is null " +
                                 " ORDER BY a.codigo asc ";

                    Query query = em.createNativeQuery(sql).setParameter(1, codigo);
                    List<Object[]> list = (List<Object[]>) query.getResultList();
                    ResumenExistencia resumen = null;
                    Integer porLlegar = null;
                    Integer porEntregar = null;
                    Integer porRecibir = null;
                    for (Object[] objecto : list) {
                        resumen = new ResumenExistencia();
                        resumen.setNombreAmbiente((String) objecto[0]);
                        resumen.setCodigoAmbiente((String) objecto[1]);
                        resumen.setCantidad((Integer) objecto[2]);
                        if (codigoAmbiente.equals(objecto[1])) {
                            porLlegar = (Integer) objecto[3];
                            porEntregar = (Integer) objecto[4];
                            porRecibir = (Integer) objecto[5];
                            resumen.setPropio(1);
                        } else {
                            resumen.setPropio(0);
                        }
                        lista.add(resumen);
                    }

                    resumen = new ResumenExistencia();
                    resumen.setNombreAmbiente("Por Llegar");
                    resumen.setCodigoAmbiente(codigoAmbiente);
                    resumen.setCantidad(porLlegar);
                    resumen.setPropio(1);
                    lista.add(resumen);
                    resumen = new ResumenExistencia();
                    resumen.setNombreAmbiente("Por Entregar");
                    resumen.setCodigoAmbiente(codigoAmbiente);
                    resumen.setCantidad(porEntregar);
                    resumen.setPropio(1);
                    lista.add(resumen);
                    resumen = new ResumenExistencia();
                    resumen.setNombreAmbiente("Por Recibir");
                    resumen.setCodigoAmbiente(codigoAmbiente);
                    resumen.setCantidad(porRecibir);
                    resumen.setPropio(1);
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
    }

    public SucursalesResponseList listaSucursales(String token) {
        SucursalesResponseList response = new SucursalesResponseList();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if (arrayId != null) {
                Integer idUsuario = (Integer) arrayId[0];
                String codigoAmbiente = (String) arrayId[1];
                List<Sucursales> lista = new ArrayList<>();
                String sql = "   SELECT a.codigo, a.nombre, a.tipo_ambiente " +
                        "     FROM ambiente a " +
                        "    WHERE a.fecha_baja is null " +
                        " ORDER BY a.nombre asc ";

                Query query = em.createNativeQuery(sql);
                List<Object[]> list = (List<Object[]>) query.getResultList();
                Sucursales sucursal = null;
                for (Object[] objecto : list) {
                    sucursal = new Sucursales();
                    sucursal.setCodigo((String) objecto[0]);
                    sucursal.setNombre((String) objecto[1]);
                    sucursal.setTipoAmbiente((String) objecto[2]);
                    lista.add(sucursal);
                }
                response.setList(lista);
                response.setRespuesta(true);
            } else {
                response.setMensaje("Las credenciales estan vencidas, ingrese desde el login nuevamente");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
