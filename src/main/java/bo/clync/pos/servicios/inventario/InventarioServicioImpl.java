package bo.clync.pos.servicios.inventario;

import bo.clync.pos.arquetipo.dto.DatosUsuario;
import bo.clync.pos.arquetipo.objetos.ResumenExistencia;
import bo.clync.pos.arquetipo.objetos.inventario.ExistenciaResponseList;
import bo.clync.pos.arquetipo.objetos.inventario.Sucursales;
import bo.clync.pos.arquetipo.objetos.inventario.SucursalesResponseList;
import bo.clync.pos.repository.common.AdmCredencialRepository;
import bo.clync.pos.repository.common.PosInventarioRepository;
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
    private AdmCredencialRepository credencialRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    public ExistenciaResponseList existenciaArticulo(String token, String codigo) {
        ExistenciaResponseList response = new ExistenciaResponseList();
            try {

                DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

                List<ResumenExistencia> resumenExistencias = new ArrayList<>();

                String sql = "   SELECT a.nombre, a.codigo, i.existencia, i.por_llegar, i.por_entregar, i.por_recibir " +
                                 "     FROM pos_ambiente a LEFT OUTER JOIN ( " +
                                 "                                  SELECT codigo_ambiente, existencia, por_llegar, por_entregar, por_recibir " +
                                 "                                    FROM pos_inventario " +
                                 "                                   WHERE codigo_articulo = ?1 " +
                                 "                                     AND fecha_baja IS NULL " +
                                 "                                      ) i " +
                                 "       ON a.codigo = i.codigo_ambiente " +
                                 "    WHERE a.fecha_baja is null " +
                                 " ORDER BY a.codigo asc ";

                Query query = em.createNativeQuery(sql).setParameter(1, codigo);

                List<Object[]> objectoResumenExistencias = (List<Object[]>) query.getResultList();

                ResumenExistencia resumen = null;
                Integer porLlegar = null;
                Integer porEntregar = null;
                Integer porRecibir = null;

                for (Object[] regResumenExist : objectoResumenExistencias) {
                    resumen = new ResumenExistencia();
                    resumen.setNombreAmbiente((String) regResumenExist[0]);
                    resumen.setCodigoAmbiente((String) regResumenExist[1]);
                    resumen.setCantidad((Integer) regResumenExist[2]);
                    if (datosUsuario.getCodigoAmbiente().equals(regResumenExist[1])) {
                        porLlegar = (Integer) regResumenExist[3];
                        porEntregar = (Integer) regResumenExist[4];
                        porRecibir = (Integer) regResumenExist[5];
                        resumen.setPropio(1);
                    } else {
                        resumen.setPropio(0);
                    }
                    resumenExistencias.add(resumen);
                }

                resumenExistencias.add(getResumenExistenciaInventario("Por Llegar", datosUsuario.getCodigoAmbiente(), porLlegar, 1));

                resumenExistencias.add(getResumenExistenciaInventario("Por Llegar", datosUsuario.getCodigoAmbiente(), porEntregar, 1));

                resumenExistencias.add(getResumenExistenciaInventario("Por Recibir", datosUsuario.getCodigoAmbiente(), porRecibir, 1));

                response.setList(resumenExistencias);

                response.setRespuesta(true);
            } catch (Exception e) {
                response.setMensaje("Error al recuperar los registros de inventario");
            }
            return response;
    }

    private ResumenExistencia getResumenExistenciaInventario(String nombre, String codigoAmbiente, Integer cantidad, int flagCtrlPropio){
        ResumenExistencia resumenExistencia = new ResumenExistencia();
        resumenExistencia.setNombreAmbiente(nombre);
        resumenExistencia.setCodigoAmbiente(codigoAmbiente);
        resumenExistencia.setCantidad(cantidad);
        resumenExistencia.setPropio(flagCtrlPropio);
        return resumenExistencia;
    }

    public SucursalesResponseList listaSucursales(String token) {
        SucursalesResponseList response = new SucursalesResponseList();
        try {

            List<Sucursales> sucursalesList = new ArrayList<>();

            String sql = "   SELECT a.codigo, a.nombre, a.tipo_ambiente " +
                         "     FROM adm_ambiente a " +
                         "    WHERE a.fecha_baja is null " +
                         " ORDER BY a.nombre asc ";

            Query query = em.createNativeQuery(sql);
            List<Object[]> ambientesList = (List<Object[]>) query.getResultList();

            for (Object[] registrAmbiente : ambientesList) {
                Sucursales sucursal = new Sucursales();
                sucursal.setCodigo((String) registrAmbiente[0]);
                sucursal.setNombre((String) registrAmbiente[1]);
                sucursal.setTipoAmbiente((String) registrAmbiente[2]);
                sucursalesList.add(sucursal);
            }
            response.setList(sucursalesList);
            response.setRespuesta(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
